package cn.cerc.ui.style;

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

public class SsrTemplate implements SsrTemplateImpl {
    private static final Logger log = LoggerFactory.getLogger(SsrTemplate.class);
    private SsrStyle style;
    private List<String> list;
    private SsrListProxy listProxy;
    private Map<String, String> map;
    private SsrMapProxy mapProxy;
    private DataRow dataRow;
    private DataSet dataSet;
    private Map<String, String> options;
    private Map<String, Supplier<String>> callback;
    private Boolean strict;
    private String templateText;
    private String id;
    private OnGetValueEvent onGetValue;
    private SsrDefine define;

    public SsrTemplate(String templateText) {
        this.templateText = templateText;
        this.style = new SsrStyle(templateText);
        style.setTemplate(this);
    }

    public SsrTemplate(Class<?> class1, String id) {
        this.templateText = SsrUtils.getTempateFileText(class1, id);
        this.style = new SsrStyle(templateText);
        style.setTemplate(this);
    }

    @Override
    public List<String> getList() {
        return list;
    }

    @Override
    public SsrTemplate toList(String... values) {
        if (list == null)
            list = new ArrayList<>();
        for (var value : values)
            list.add(value);
        return this;
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    public SsrTemplate setMap(Map<String, String> map) {
        this.map = map;
        return this;
    }

    @Override
    public SsrTemplate toMap(String Key, String value) {
        if (map == null)
            map = new LinkedHashMap<>();
        map.put(Key, value);
        return this;
    }

    @Override
    public SsrTemplateImpl setOption(String key, String value) {
        if (options == null)
            options = new HashMap<>();
        options.put(key, value);
        return this;
    }

    @Override
    public Optional<String> getOption(String key) {
        Optional<String> result = Optional.empty();
        if (options != null)
            result = Optional.ofNullable(options.get(key));
        if (result.isEmpty() && define != null)
            result = define.getOption(key);
        return result;
    }

    @Override
    @Deprecated
    public Map<String, String> getOptions() {
        if (options == null)
            options = new HashMap<>();
        return options;
    }

    @Override
    public DataRow getDataRow() {
        if (dataRow != null)
            return dataRow;
        if (define != null)
            return define.getDataRow();
        else
            return null;
    }

    @Override
    public SsrTemplate setDataRow(DataRow dataRow) {
        this.dataRow = dataRow;
        return this;
    }

    @Override
    public SsrTemplate setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    @Override
    public DataSet getDataSet() {
        if (dataSet != null)
            return dataSet;
        if (define != null)
            return define.getDataSet();
        else
            return null;
    }

    @Override
    public String getHtml() {
        var sb = new StringBuffer();
        for (var node : this.style)
            sb.append(node.getHtml(this));
        return sb.toString();
    }

    /**
     * 返回当前解析模式设置
     * 
     * @return 严格格式还是宽松模式，默认为严格模式
     */
    @Override
    public boolean isStrict() {
        if (strict != null)
            return strict.booleanValue();
        if (define != null)
            return define.isStrict();
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
    public SsrTemplate setStrict(boolean strict) {
        this.strict = strict;
        return this;
    }

    @Override
    public String templateText() {
        return this.templateText;
    }

    @Override
    public SsrTemplate setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public SsrMapProxy getMapProxy() {
        if (this.map == null)
            return null;
        if (this.mapProxy == null)
            this.mapProxy = new SsrMapProxy(this.map);
        return mapProxy;
    }

    @Override
    public SsrListProxy getListProxy() {
        if (this.list == null)
            return null;
        if (this.listProxy == null)
            this.listProxy = new SsrListProxy(this.list);
        return listProxy;
    }

    @Override
    public SsrTemplateImpl onCallback(String nodeId, Supplier<String> supplier) {
        if (this.callback == null)
            callback = new HashMap<>();
        callback.put(nodeId, supplier);
        return this;
    }

    @Override
    public Map<String, Supplier<String>> getCallback() {
        return callback;
    }

    @Override
    public SsrStyle style() {
        return style;
    }

    @Override
    public SsrTemplate setStyle(SsrStyle style) {
        Objects.requireNonNull(style);
        this.style = style;
        style.setTemplate(this);
        return this;
    }

    @Override
    public Optional<String> getValue(String field) {
        String result = null;
        var dataRow = this.getDataRow();
        var dataSet = this.getDataSet();
        // 先查找list
        if (list != null && SsrListIndexNode.is(field))
            result = this.getListProxy().index();
        else if (list != null && SsrListValueNode.is(field))
            result = this.getListProxy().value();
        else if (list != null && Utils.isNumeric(field)) {
            var index = Integer.parseInt(field);
            if (index >= 0 && index < list.size()) {
                result = list.get(index);
            } else if (!this.isStrict()) {
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
                log.warn("map and dataRow exists field: {}", field);
            if (dataSet != null && dataSet.exists(field))
                log.warn("map and dataSet exists field: {}", field);
            result = map.get(field);
        }
        // 再查找其它
        else if (dataRow != null && dataRow.exists(field)) {
            if (dataSet != null && dataSet.exists(field))
                log.warn("dataRow and dataSet exists field: {}", field);
            result = dataRow.getText(field);
        } else if (dataSet != null && dataSet.exists(field)) {
            var row = dataSet.currentRow();
            result = row.isPresent() ? row.get().getText(field) : "";
        } else if (options != null && options.containsKey(field)) {
            result = options.get(field);
        } else if (!isStrict()) {
            result = "";
        } else if (onGetValue == null) {
            log.error("not find field: {}", field);
        }
        if (onGetValue != null)
            result = onGetValue.getValue(field, result);
        return Optional.ofNullable(result);
    }

    @Override
    public SsrTemplateImpl onGetValue(OnGetValueEvent onGetValue) {
        this.onGetValue = onGetValue;
        return this;
    }

    protected SsrTemplate setDefine(SsrDefine define) {
        this.define = define;
        return this;
    }

}
