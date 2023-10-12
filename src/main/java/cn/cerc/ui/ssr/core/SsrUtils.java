package cn.cerc.ui.ssr.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.cerc.db.core.ClassData;
import cn.cerc.db.core.Datetime;
import cn.cerc.db.core.FastDate;
import cn.cerc.db.core.FastTime;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.source.Binder;

public class SsrUtils {
    private static final Logger log = LoggerFactory.getLogger(SsrUtils.class);

    public static String fixSpace(String text) {
        if (text.length() == 0)
            return "";
        var value = text.trim();
//        if (value.trim().startsWith("<")) {
//            int startIndex = 0;
//            while (startIndex < value.length() && Character.isWhitespace(value.charAt(startIndex))) {
//                startIndex++;
//            }
//            value = value.substring(startIndex);
//        }
//        if (value.trim().endsWith(">")) {
//            int endIndex = value.length() - 1;
//            while (endIndex > 0 && Character.isWhitespace(value.charAt(endIndex))) {
//                endIndex--;
//            }
//            value = value.substring(0, endIndex + 1);
//        }

        if (value.length() == 0)
            return " ";

        boolean find = false;
        StringBuffer sb = new StringBuffer();
        for (var i = 0; i < value.length(); i++) {
            char tmp = value.charAt(i);
            if (tmp == ' ') {
                if (find)
                    continue;
                find = true;
                sb.append(tmp);
            } else if (tmp == '\n') {
                if (find)
                    sb.deleteCharAt(sb.length() - 1);
                sb.append(tmp);
                find = true;
            } else {
                find = false;
                sb.append(tmp);
            }
        }
        var tmp = text.charAt(text.length() - 1);
        if (tmp == '\n' || tmp == ' ')
            sb.append(tmp);
        return sb.toString();
    }

    /**
     * 
     * @param templateText
     * @return 根据模版创建 ssr 节点
     */
    public static ArrayList<ISsrNode> createNodes(String templateText) {
        var nodes = new ArrayList<ISsrNode>();
        int start, end;
        var line = templateText.trim();
        while (line.length() > 0) {
            if ((start = line.indexOf("${")) > -1 && (end = line.indexOf("}", start)) > -1) {
                if (start > 0) {
                    var text = "";
                    if (line.charAt(start - 1) == '\n')
                        text = line.substring(0, start - 1);
                    else
                        text = line.substring(0, start);
                    if (text.length() > 0)
                        nodes.add(new SsrTextNode(fixSpace(text)));
                }
                var text = line.substring(start + 2, end);
                if (SsrCallbackNode.is(text))
                    nodes.add(new SsrCallbackNode(text));
                else if (SsrListIndexNode.is(text))
                    nodes.add(new SsrListIndexNode(text));
                else if (SsrListValueNode.is(text))
                    nodes.add(new SsrListValueNode(text));
                else if (SsrMapIndexNode.is(text))
                    nodes.add(new SsrMapIndexNode(text));
                else if (SsrMapKeyNode.is(text))
                    nodes.add(new SsrMapKeyNode(text));
                else if (SsrMapValueNode.is(text))
                    nodes.add(new SsrMapValueNode(text));
                else if (SsrDataSetRecNode.is(text))
                    nodes.add(new SsrDataSetRecNode(text));
                else if (SsrDataSetItemNode.is(text))
                    nodes.add(new SsrDataSetItemNode(text));
                else
                    nodes.add(new SsrValueNode(text));
                // 解决上一行的最后一个字符为换行符
                if (end + 1 < line.length() && line.charAt(end + 1) == '\n')
                    end++;
                line = line.substring(end + 1, line.length());
            } else {
                var text = fixSpace(line);
                if (text.length() > 0)
                    nodes.add(new SsrTextNode(line));
                break;
            }
        }
        return nodes;
    }

    /**
     * 
     * @param class1
     * @param id
     * @return 查找类所在目录下的同名文件，并返回相应的html文件内容
     */
    public static String getTempateFileText(Class<?> class1, String id) {
        var fileName = class1.getSimpleName() + ".html";
        if (!Utils.isEmpty(id))
            fileName = class1.getSimpleName() + "_" + id + ".html";

        var file = class1.getResourceAsStream(fileName);
        var list = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8));
        String line;
        var sb = new StringBuffer();
        boolean start = false;
        try {
            while ((line = list.readLine()) != null) {
                var text = line;
                if ("<body>".equals(text))
                    start = true;
                else if ("</body>".equals(text))
                    break;
                else if (start)
                    sb.append(text);
            }
            return sb.toString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    public record PropertiesValue(Object object, boolean container, boolean visual) {
    }

    public static final List<Field> getFieldList(Class<?> clazz) {
        List<Field> list = new ArrayList<>();
        addFieldList(list, clazz);
        return list;
    }

    private static void addFieldList(List<Field> list, Class<?> clazz) {
        for (var field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                // 开放读取权限
                if (field.getModifiers() == ClassData.DEFAULT || field.getModifiers() == ClassData.PRIVATE
                        || field.getModifiers() == ClassData.PROTECTED)
                    field.setAccessible(true);
                if (field.getType() == String.class || field.getType() == Integer.class || field.getType() == int.class)
                    list.add(field);
                else if (field.getType() == boolean.class || field.getType() == Boolean.class)
                    list.add(field);
                else if (field.getType() == float.class || field.getType() == Float.class)
                    list.add(field);
                else if (field.getType() == double.class || field.getType() == Double.class)
                    list.add(field);
                else if (field.getType().isEnum())
                    list.add(field);
                else if (field.getType() == Binder.class)
                    list.add(field);
                else if (field.getType() == EntityServiceRecord.class)
                    list.add(field);
                else
                    log.error("不支持的字段类型：{}: {}", field.getName(), field.getType().getSimpleName());
            }
        }
        if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class)
            addFieldList(list, clazz.getSuperclass());
    }

    public static void readProperty(Object properties, Field field, JsonNode json) {
        try {
            JsonNode value = json.get(field.getName());
            if (value == null)
                return;
            if (field.getType() == Binder.class) {
                Binder<?> binder = (Binder<?>) field.get(properties);
                binder.targetId(value.asText());
            } else if (field.getType().isEnum()) {
                Enum<?>[] enums = (Enum<?>[]) field.getType().getEnumConstants();
                if (Utils.isNumeric(value.asText())) {
                    int index = value.asInt();
                    if (enums.length <= index)
                        index = 0;
                    field.set(properties, enums[index]);
                } else {
                    Enum<?> defaultValue = enums[0];
                    for (Enum<?> item : enums) {
                        if (item.name().equals(value.asText())) {
                            defaultValue = item;
                            break;
                        }
                    }
                    field.set(properties, defaultValue);
                }
            } else if (field.getType() == EntityServiceRecord.class) {
                JsonNode temp = json.get(field.getName() + "_name");
                String desc = value.asText();
                if (temp != null && !Utils.isEmpty(temp.asText()))
                    desc = temp.asText();
                field.set(properties, new EntityServiceRecord(value.asText(), desc));
            } else
                writeToObject(properties, field, value);
            return;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
            return;
        }
    }

    public static void writeToObject(Object object, Field field, JsonNode node) throws IllegalAccessException {
        if (field.getType() == boolean.class)
            field.setBoolean(object, node.asBoolean());
        else if (field.getType() == int.class)
            field.setInt(object, node.asInt());
        else if (field.getType() == long.class)
            field.setLong(object, node.asLong());
        else if (field.getType() == float.class)
            field.setFloat(object, Float.parseFloat(node.asText()));
        else if (field.getType() == double.class)
            field.setDouble(object, node.asDouble());
        else if (field.getType() == Boolean.class)
            field.set(object, Boolean.valueOf(node.asBoolean()));
        else if (field.getType() == Integer.class)
            field.set(object, Integer.valueOf(node.asInt()));
        else if (field.getType() == Long.class)
            field.set(object, Long.valueOf(node.asLong()));
        else if (field.getType() == Float.class)
            field.set(object, Float.valueOf(node.asText()));
        else if (field.getType() == Double.class)
            field.set(object, Double.valueOf(node.asDouble()));
        else if (field.getType() == Datetime.class)
            field.set(object, new Datetime(node.asText()));
        else if (field.getType() == FastDate.class)
            field.set(object, new FastDate(node.asText()));
        else if (field.getType() == FastTime.class)
            field.set(object, new FastTime(node.asText()));
        else if (field.getType() == String.class)
            field.set(object, node.asText());
        else if (field.getType() == Binder.class) {
            if (object instanceof Binder<?> binder)
                binder.targetId(node.asText());
        } else if ("this$0".equals(field.getName())) {
        } else {
            log.warn(String.format("writer error: %s.%s", field.getType().getName(), field.getName()));
        }
    }

    public static void copyNode(JsonNode source, ObjectNode target) {
        var names = source.fieldNames();
        while (names.hasNext()) {
            var name = names.next();
            if (name.startsWith("v_"))
                target.put(name, source.get(name).asText());
        }
    }

}
