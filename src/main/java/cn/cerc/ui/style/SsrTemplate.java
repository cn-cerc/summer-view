package cn.cerc.ui.style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;

public class SsrTemplate implements SsrTemplateImpl, Iterable<SsrNodeImpl> {
//    private static final Logger log = LoggerFactory.getLogger(SsrTemplate.class);
    private ArrayList<SsrNodeImpl> nodes;
    private List<String> list;
    private ListProxy listProxy;
    private Map<String, String> map;
    private MapProxy mapProxy;
    private DataRow dataRow;
    private DataSet dataSet;
    private Map<String, String> options = new HashMap<>();
    private Map<String, Supplier<String>> callback;
    private boolean strict = true;
    private String templateText;
    private String id;

    public SsrTemplate(String templateText) {
        super();
        this.templateText = templateText;
        this.nodes = SsrUtils.createNodes(templateText);
        for (var node : nodes)
            node.setTemplate(this);
        CompressNodes.run(nodes);
    }

    public SsrTemplate(Class<?> class1, String id) {
        this.templateText = SsrUtils.getTempateFileText(class1, id);
        this.nodes = SsrUtils.createNodes(templateText);
        for (var node : nodes)
            node.setTemplate(this);
        CompressNodes.run(nodes);
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
    public Map<String, String> getOptions() {
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
        for (var node : this.nodes)
            sb.append(node.getHtml());
        return sb.toString();
    }

    protected List<SsrNodeImpl> nodes() {
        return nodes;
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

    @Override
    public Iterator<SsrNodeImpl> iterator() {
        return nodes.iterator();
    }

}
