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
    private SsrStyle block;
    private List<String> list;
    private ListProxy listProxy;
    private Map<String, String> map;
    private MapProxy mapProxy;
    private DataRow dataRow;
    private DataSet dataSet;
    private Map<String, String> options;
    private Map<String, Supplier<String>> callback;
    private boolean strict = true;
    private String templateText;
    private String id;

    public SsrTemplate(String templateText) {
        super();
        this.templateText = templateText;
        this.block = new SsrStyle(templateText);
        block.setTemplate(this);
    }

    public SsrTemplate(Class<?> class1, String id) {
        this.templateText = SsrUtils.getTempateFileText(class1, id);
        this.block = new SsrStyle(templateText);
        block.setTemplate(this);
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
        if (options == null)
            return Optional.empty();
        return Optional.ofNullable(options.get(key));
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
        return dataRow;
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
        return dataSet;
    }

    @Override
    public String getHtml() {
        var sb = new StringBuffer();
        for (var node : this.block.nodes)
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
        return strict;
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

    public class MapProxy {
        private Map<String, String> map;
        private int rec;

        public MapProxy(Map<String, String> map) {
            this.map = map;
            rec = -1;
        }

        public void reset() {
            rec = -1;
        }

        public boolean fetch() {
            rec++;
            return map != null && rec > -1 && rec < map.size();
        }

        public String key() {
            if (map != null) {
                var i = 0;
                for (var key : map.keySet()) {
                    if (i == rec)
                        return key;
                    i++;
                }
            }
            return null;
        }

        public String value() {
            if (map != null) {
                var i = 0;
                for (var key : map.keySet()) {
                    if (i == rec)
                        return map.get(key);
                    i++;
                }
            }
            return null;
        }
    }

    public class ListProxy {
        private List<String> list;
        private int rec;

        public ListProxy(List<String> list) {
            this.list = list;
        }

        public void reset() {
            rec = -1;
        }

        public boolean fetch() {
            rec++;
            return list != null && rec > -1 && rec < list.size();
        }

        public String item() {
            if (list != null && rec > -1 && rec < list.size())
                return list.get(rec);
            else
                return null;
        }
    }

    @Override
    public MapProxy getMapProxy() {
        if (this.map == null)
            return null;
        if (this.mapProxy == null)
            this.mapProxy = new MapProxy(this.map);
        return mapProxy;
    }

    @Override
    public ListProxy getListProxy() {
        if (this.list == null)
            return null;
        if (this.listProxy == null)
            this.listProxy = new ListProxy(this.list);
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

    public SsrStyle block() {
        return block;
    }

    public SsrTemplate setBlock(SsrStyle block) {
        Objects.requireNonNull(block);
        this.block = block;
        block.setTemplate(this);
        return this;
    }

    @Override
    public Optional<String> getValue(String field) {
        // 先查找list
        if (list != null) {
            if (SsrListItemNode.is(field))
                return Optional.ofNullable(this.getListProxy().item());
            else if (Utils.isNumeric(field)) {
                var index = Integer.parseInt(field);
                if (index >= 0 && index < list.size()) {
                    return Optional.ofNullable(list.get(index));
                } else if (!this.strict) {
                    return Optional.ofNullable("");
                } else {
                    log.error("not find index of list: {}", field);
                    return Optional.empty();
                }
            }
        }
        // 再查找map
        if (map != null) {
            if (SsrMapKeyNode.is(field))
                return Optional.ofNullable(this.getMapProxy().key());
            else if (SsrMapValueNode.is(field))
                return Optional.ofNullable(this.getMapProxy().value());
            else if (map.containsKey(field)) {
                if (dataRow != null && dataRow.exists(field))
                    log.warn("map and dataRow exists field: {}", field);
                if (dataSet != null && dataSet.exists(field))
                    log.warn("map and dataSet exists field: {}", field);
                return Optional.ofNullable(map.get(field));
            }
        }

        // 再查找其它
        if (dataRow != null && dataRow.exists(field)) {
            if (dataSet != null && dataSet.exists(field))
                log.warn("dataRow and dataSet exists field: {}", field);
            return Optional.ofNullable(dataRow.getText(field));
        } else if (dataSet != null && dataSet.exists(field)) {
            var row = dataSet.currentRow();
            return Optional.ofNullable(row.isPresent() ? row.get().getText(field) : "");
        } else if (options != null && options.containsKey(field)) {
            return Optional.ofNullable(options.get(field));
        } else if (!strict) {
            return Optional.ofNullable("");
        } else {
            log.error("not find field: {}", field);
            return Optional.empty();
        }
    }

}
