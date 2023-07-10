package cn.cerc.ui.style;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;

public class SsrTemplate implements SsrTemplateImpl {
    private ArrayList<SsrNodeImpl> nodes;
    private List<String> list;
    private Map<String, String> map;
    private DataRow dataRow;
    private DataSet dataSet;
    private boolean strict = true;
    private String templateText;
    private SsrCallbackImpl callback;

    public SsrTemplate(String templateText) {
        super();
        setTemplateText(templateText);
    }

    public SsrTemplate(Class<?> class1, String id) {
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
                if ("<body>".equals(line))
                    start = true;
                else if ("</body>".equals(line))
                    break;
                else if (start)
                    sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setTemplateText(sb.toString());
    }

    @Override
    public SsrTemplate setList(List<String> list) {
        this.list = list;
        return this;
    }

    @Override
    public SsrTemplate setMap(Map<String, String> map) {
        this.map = map;
        return this;
    }

    @Override
    public SsrTemplate setDataRow(DataRow dataRow) {
        if (dataSet != null)
            throw new RuntimeException("dataSet is not null");
        this.dataRow = dataRow;
        return this;
    }

    @Override
    public SsrTemplate setDataSet(DataSet dataSet) {
        if (dataRow != null)
            throw new RuntimeException("dataRow is not null");
        this.dataSet = dataSet;
        return this;
    }

    @Override
    public String getHtml() {
        var sb = new StringBuffer();
        for (var node : this.nodes)
            sb.append(node.getHtml());
        return sb.toString();
    }

    private void compressNodes(ArrayList<SsrNodeImpl> nodes, String startFlag, String endFlag, SsrForeachImpl supper) {
        // 先复制到 temp 变量中
        var temp = new ArrayList<SsrNodeImpl>();
        temp.addAll(nodes);
        nodes.clear();
        // 开始归集
        SsrForeachNode container = null;
        for (var node : temp) {
            if (node instanceof SsrValueNode item) {
                if (item.getField().startsWith(startFlag)) {
                    container = supper.createObject(item.getField());
                    container.setTemplate(this);
                    nodes.add(container);
                    continue;
                } else if (item.getField().startsWith(endFlag)) {
                    container = null;
                    continue;
                }
            }
            if (container != null) {
                container.addItem(node);
            } else {
                nodes.add(node);
            }
        }
    }

    private ArrayList<SsrNodeImpl> createNodes(String templateText) {
        var list = new ArrayList<SsrNodeImpl>();
        int start, end;
        var line = templateText;
        while (line.length() > 0) {
            if ((start = line.indexOf("${")) > -1 && (end = line.indexOf("}", start)) > -1) {
                if (start > 0)
                    list.add(new SsrTextNode(line.substring(0, start)));
                var text = line.substring(start + 2, end);
                if (text.startsWith("callback"))
                    list.add(new SsrCallbackNode(text));
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

    public List<SsrNodeImpl> getNodes() {
        return nodes;
    }

    @Override
    public DataRow getDataRow() {
        return dataSet != null ? dataSet.currentRow().get() : dataRow;
    }

    @Override
    public List<String> getList() {
        return list;
    }

    @Override
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    public DataSet getDataSet() {
        return dataSet;
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
    public SsrTemplate setTemplateText(String templateText) {
        this.templateText = templateText;
        this.nodes = this.createNodes(templateText);
        compressNodes(nodes, SsrIfNode.StartFlag, SsrIfNode.EndFlag, (text) -> new SsrIfNode(text));
        compressNodes(nodes, SsrListNode.StartFlag, SsrListNode.EndFlag, (text) -> new SsrListNode(text));
        compressNodes(nodes, SsrMapNode.StartFlag, SsrMapNode.EndFlag, (text) -> new SsrMapNode(text));
        compressNodes(nodes, SsrDatasetNode.StartFlag, SsrDatasetNode.EndFlag, (text) -> new SsrDatasetNode(text));
        return this;
    }

    protected String getTemplateText() {
        return templateText;
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

    @Override
    public Optional<String> getValue(String field) {
        var dataRow = this.getDataRow();
        var map = this.getMap();
        if (map != null && map.containsKey(field)) {
            Object val = map.get(field);
            return Optional.ofNullable(val != null ? val.toString() : "");
        }
        if (dataRow.exists(field))
            return Optional.of(dataRow.getText(field));
        else
            return Optional.empty();
    }

}
