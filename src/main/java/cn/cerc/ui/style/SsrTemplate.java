package cn.cerc.ui.style;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;

public class SsrTemplate implements SsrTemplateImpl {
//    private static final Logger log = LoggerFactory.getLogger(SsrTemplate.class);
    private ArrayList<SsrNodeImpl> nodes;
    private List<String> list;
    private Map<String, String> map;
    private DataRow dataRow;
    private DataSet dataSet;
    private boolean strict = true;
    private SsrCallbackImpl callback;
    private String templateText;
    private String id;

    public SsrTemplate(String templateText) {
        super();
        this.templateText = templateText;
        this.nodes = createNodes(templateText);
        CompressNodes.run(nodes);
    }

    public SsrTemplate(Class<?> class1, String id) {
        this.templateText = SsrUtils.getTempateFileText(class1, id);
        this.nodes = createNodes(templateText);
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
    @Deprecated
    public SsrTemplate setList(List<String> list) {
        this.list = list;
        return this;
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    public SsrTemplate toMap(String Key, String value) {
        if (map == null)
            map = new LinkedHashMap<>();
        map.put(Key, value);
        return this;
    }

    @Override
    @Deprecated
    public SsrTemplate setMap(Map<String, String> map) {
        this.map = map;
        return this;
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

    public List<SsrNodeImpl> getNodes() {
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
    public SsrTemplateImpl setCallback(SsrCallbackImpl callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public SsrCallbackImpl getCallback() {
        return callback;
    }

    private ArrayList<SsrNodeImpl> createNodes(String templateText) {
        var list = new ArrayList<SsrNodeImpl>();
        int start, end;
        var line = templateText.trim();
        while (line.length() > 0) {
            if ((start = line.indexOf("${")) > -1 && (end = line.indexOf("}", start)) > -1) {
                if (start > 0)
                    list.add(new SsrTextNode(line.substring(0, start)));
                var text = line.substring(start + 2, end);
                if (SsrCallbackNode.is(text))
                    list.add(new SsrCallbackNode(text));
                else if (SsrDataSetRecNode.is(text))
                    list.add(new SsrDataSetRecNode(text));
                else if (SsrDataSetItemNode.is(text))
                    list.add(new SsrDataSetItemNode(text));
                else
                    list.add(new SsrValueNode(text));
                line = line.substring(end + 1, line.length());
            } else {
                list.add(new SsrTextNode(line));
                break;
            }
        }
        list.forEach(item -> item.setTemplate(this));
        return list;
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
    public Optional<String> getId() {
        return Optional.ofNullable(this.id);
    }

}
