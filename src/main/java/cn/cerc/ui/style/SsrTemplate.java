package cn.cerc.ui.style;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;

public class SsrTemplate implements SsrTemplateImpl {
    private ArrayList<SsrNodeImpl> nodes;
    private DataRow dataRow;
    private DataSet dataSet;
    private List<String> list;
    private Map<String, String> map;
    private String[] params;

    public SsrTemplate(String templateText) {
        super();
        initNodes(templateText);
    }

    public SsrTemplate(Class<?> class1, String id) {
        var fileName = class1.getSimpleName() + "_" + id + ".html";
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
        initNodes(sb.toString());
    }

    private void initNodes(String templateText) {
        this.nodes = this.createNodes(templateText);
        compressNodes(nodes, SsrIfNode.StartFlag, SsrIfNode.EndFlag, (text) -> new SsrIfNode(text));
        compressNodes(nodes, SsrListNode.StartFlag, SsrListNode.EndFlag, (text) -> new SsrListNode(text));
        compressNodes(nodes, SsrMapNode.StartFlag, SsrMapNode.EndFlag, (text) -> new SsrMapNode(text));
        compressNodes(nodes, SsrDatasetNode.StartFlag, SsrDatasetNode.EndFlag, (text) -> new SsrDatasetNode(text));
    }

    public SsrTemplate setParams(String... params) {
        this.params = params;
        return this;
    }

    public SsrTemplate setList(List<String> list) {
        this.list = list;
        return this;
    }

    public SsrTemplate setMap(Map<String, String> map) {
        this.map = map;
        return this;
    }

    public SsrTemplate setDataRow(DataRow dataRow) {
        if (dataSet != null)
            throw new RuntimeException("dataSet is not null");
        this.dataRow = dataRow;
        return this;
    }

    public SsrTemplate setDataSet(DataSet dataSet) {
        if (dataRow != null)
            throw new RuntimeException("dataRow is not null");
        this.dataSet = dataSet;
        return this;
    }

    public String html() {
        var sb = new StringBuffer();
        for (var node : this.nodes)
            sb.append(node.getValue());
        return sb.toString();
    }

    private void compressNodes(ArrayList<SsrNodeImpl> nodes, String startFlag, String endFlag,
            SsrForeachImpl supper) {
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
                list.add(new SsrValueNode(line.substring(start + 2, end)));
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
    public String[] getParams() {
        return params;
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

}
