package cn.cerc.ui.ssr.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.local.tool.JsonTool;
import cn.cerc.mis.log.JayunLogParser;
import cn.cerc.ui.ssr.editor.ISsrBoard;

public class SsrBlock implements ISsrOption {
    private static final Logger log = LoggerFactory.getLogger(SsrBlock.class);
    public static final String Fields = "fields";
    private SsrNodes nodes;
    private List<String> list;
    private SsrListProxy listProxy;
    private Map<String, String> map;
    private SsrMapProxy mapProxy;
    private DataRow dataRow;
    private DataSet dataSet;
    private Map<String, String> options;
    private Map<String, Supplier<String>> callback;
    private Boolean strict;
    private String text;
    private String id;
    private OnGetValueEvent onGetValue;
    private SsrTemplate template;
    private Class<?> origin;

    public SsrBlock() {
    }

    public SsrBlock(String templateText) {
        this.text(templateText);
    }

    public SsrBlock(Class<?> class1, String id) {
        this.text(SsrUtils.getTempateFileText(class1, id));
        this.origin = class1;
    }

    /**
     * 请改使用 toMap
     */
    @Deprecated
    public SsrBlock setMap(Map<String, String> map) {
        this.map = map;
        return this;
    }

    @Override
    public Optional<String> option(String key) {
        Optional<String> result = Optional.empty();
        if (options != null)
            result = Optional.ofNullable(options.get(key));
        if (result.isEmpty() && template != null)
            result = template.option(key);
        return result;
    }

    @Override
    public SsrBlock option(String key, String value) {
        if (options == null)
            options = new HashMap<>();
        if (value == null)
            options.remove(key);
        else
            options.put(key, value);
        return this;
    }

    public DataRow dataRow() {
        if (dataRow != null)
            return dataRow;
        if (template != null)
            return template.dataRow();
        else
            return null;
    }

    public SsrBlock dataRow(DataRow dataRow) {
        this.dataRow = dataRow;
        return this;
    }

    public DataSet dataSet() {
        if (dataSet != null)
            return dataSet;
        if (template != null)
            return template.dataSet();
        else
            return null;
    }

    public SsrBlock dataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    /**
     * 
     * @return 返回模版的解析结果
     */
    public String html() {
        var sb = new StringBuffer();
        for (var node : this.nodes)
            sb.append(node.getHtml(this));
        return sb.toString();
    }

    /**
     * 返回当前解析模式设置
     * 
     * @return 严格格式还是宽松模式，默认为严格模式
     */
    @Override
    public boolean strict() {
        if (strict != null)
            return strict.booleanValue();
        if (template != null)
            return template.strict();
        else
            return true;
    }

    /**
     * 严格模式：所填写的参数必须存在<br>
     * 宽松模式：若参数不存在，则显示为空
     * 
     * @param strict 是否为严格模式
     */
    @Override
    public SsrBlock strict(boolean strict) {
        this.strict = strict;
        return this;
    }

    public String text() {
        return this.text;
    }

    public SsrBlock text(String text) {
        this.text = text;
        this.nodes = new SsrNodes(text);
        return this;
    }

    public SsrBlock id(String id) {
        this.id = id;
        return this;
    }

    public String id() {
        return this.id;
    }

    public SsrMapProxy getMapProxy() {
        if (this.map == null)
            return null;
        if (this.mapProxy == null)
            this.mapProxy = new SsrMapProxy(this.map);
        return mapProxy;
    }

    public SsrListProxy getListProxy() {
        if (this.list == null)
            return null;
        if (this.listProxy == null)
            this.listProxy = new SsrListProxy(this.list);
        return listProxy;
    }

    /**
     * @param nodeId   节点id
     * @param supplier 赋值回调函数
     * @return 设置回调函数
     */
    public SsrBlock onCallback(String nodeId, Supplier<String> supplier) {
        if (this.callback == null)
            callback = new HashMap<>();
        callback.put(nodeId, supplier);
        return this;
    }

    /**
     * 
     * @return 返回回调函数
     */
    public Map<String, Supplier<String>> callback() {
        return callback;
    }

    public SsrNodes nodes() {
        return nodes;
    }

    public SsrBlock nodes(SsrNodes nodes) {
        Objects.requireNonNull(nodes);
        this.nodes = nodes;
        return this;
    }

    public Optional<String> getValue(String field) {
        String result = null;
        var dataRow = this.dataRow();
        var dataSet = this.dataSet();
        // 先查找list
        if (list != null && SsrListIndexNode.is(field))
            result = this.getListProxy().index();
        else if (list != null && SsrListValueNode.is(field))
            result = this.getListProxy().value();
        else if (list != null && Utils.isNumeric(field)) {
            var index = Integer.parseInt(field);
            if (index >= 0 && index < list.size()) {
                result = list.get(index);
            } else if (!this.strict()) {
                result = "";
            } else {
                log.error("not find index of list: {}", field);
            }
        }
        // 再查找map
        else if (map != null && SsrMapIndexNode.is(field))
            result = this.getMapProxy().index();
        else if (map != null && SsrMapKeyNode.is(field))
            result = this.getMapProxy().key();
        else if (map != null && SsrMapValueNode.is(field))
            result = this.getMapProxy().value();
        else if (map != null && map.containsKey(field)) {
            if (dataRow != null && dataRow.exists(field))
                fieldNotFindLog(field, false);
            if (dataSet != null && dataSet.exists(field))
                fieldNotFindLog(field, false);
            result = map.get(field);
        }
        // 再查找其它
        else if (dataRow != null && dataRow.exists(field)) {
            if (dataSet != null && dataSet.exists(field))
                fieldNotFindLog(field, false);
            result = dataRow.getText(field);
        } else if (dataSet != null && dataSet.exists(field)) {
            var row = dataSet.currentRow();
            result = row.isPresent() ? row.get().getText(field) : "";
        } else if (options != null && options.containsKey(field)) {
            result = options.get(field);
        } else if (!strict()) {
            result = "";
        } else if (onGetValue == null) {
            fieldNotFindLog(field, true);
        }
        if (onGetValue != null)
            result = onGetValue.getValue(field, result);
        return Optional.ofNullable(result);
    }

    private void fieldNotFindLog(String field, boolean isError) {
        String templateId = Optional.ofNullable(template).map(SsrTemplate::id).orElse("null");
        String originName = Optional.ofNullable(origin).map(Class::getName).orElse("null");
        String listValue = Optional.ofNullable(list).map(JsonTool::toJson).orElse("null");
        String mapValue = Optional.ofNullable(map).map(JsonTool::toJson).orElse("null");
        String optionValue = Optional.ofNullable(options).map(JsonTool::toJson).orElse("null");
        String message = String.format(
                "not find field %s, id %s, templateId %s, origin %s, dataRow %s, dataSet %s, list %s, map %s, option %s",
                field, id, templateId, originName, dataRow, dataSet, listValue, mapValue, optionValue);
        if (isError) {
            JayunLogParser.error(SsrBlock.class, new RuntimeException(this.text), message);
        } else {
            JayunLogParser.warn(SsrBlock.class, new RuntimeException(this.text), message);
        }
    }

    public SsrBlock onGetValue(OnGetValueEvent onGetValue) {
        this.onGetValue = onGetValue;
        return this;
    }

    public SsrBlock template(SsrTemplate template) {
        this.template = template;
        return this;
    }

    /**
     * 固定当前查询字段（不会出现在配置列表中）
     */
    public SsrBlock fixed(ISsrBoard form) {
        this.option("option", null);
        form.addColumn(this.id);
        return this;
    }

    public List<String> list() {
        return this.list;
    }

    public SsrBlock toList(String... values) {
        if (list == null)
            list = new ArrayList<>();
        for (var value : values)
            list.add(value);
        return this;
    }

    public SsrBlock toList(List<String> list) {
        if (list == null) {
            var e = new RuntimeException("list 不允许为空");
            log.warn(e.getMessage(), e);
            return this;
        }
        if (this.list == null)
            this.list = new ArrayList<>();
        this.list.addAll(list);
        return this;
    }

    public SsrBlock toList(Enum<?>[] enums) {
        if (list == null)
            list = new ArrayList<>();
        for (var item : enums)
            list.add(item.name());
        return this;
    }

    public Map<String, String> map() {
        return map;
    }

    public SsrBlock toMap(String Key, String value) {
        if (map == null)
            map = new LinkedHashMap<>();
        map.put(Key, value);
        return this;
    }

    public SsrBlock toMap(Map<String, String> map) {
        if (map == null) {
            var e = new RuntimeException("map 不允许为空");
            log.warn(e.getMessage(), e);
            return this;
        }
        if (this.map == null)
            this.map = new LinkedHashMap<>();
        this.map.putAll(map);
        return this;
    }

    /**
     * 请改使用 toList
     */
    @Deprecated
    public SsrBlock toMap(Enum<?>[] enums) {
        for (var item : enums)
            toMap("" + item.ordinal(), item.name());
        return this;
    }

    /**
     * 
     * @param fields 设置字段列表，以逗号隔开
     * @return 返回自身
     */
    public SsrBlock fields(String... fields) {
        this.option(Fields, String.join(",", fields));
        return this;
    }

}
