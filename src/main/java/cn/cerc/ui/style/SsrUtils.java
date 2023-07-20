package cn.cerc.ui.style;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;

public class SsrUtils {
    private static final Logger log = LoggerFactory.getLogger(SsrUtils.class);

    public static String fixSpace(String text) {
        if (text.length() == 0)
            return "";
        var value = text.trim();
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
    public static ArrayList<SsrNodeImpl> createNodes(String templateText) {
        var nodes = new ArrayList<SsrNodeImpl>();
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
                else if (SsrListItemNode.is(text))
                    nodes.add(new SsrListItemNode(text));
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

    public static void setConfig(SsrComponentImpl search, DataSet configs) {
        configs.forEach(item -> {
            if (item.getEnum("option_", TemplateConfigOptionEnum.class) != TemplateConfigOptionEnum.不显示)
                search.addField(item.getString("column_name_"));
        });
    }

}
