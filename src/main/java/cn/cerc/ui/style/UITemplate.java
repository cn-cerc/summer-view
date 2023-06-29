package cn.cerc.ui.style;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;

public class UITemplate {
    private static final Logger log = LoggerFactory.getLogger(UITemplate.class);
    private ArrayList<UISsrNodeImpl> nodes;
    private DataRow dataRow;
    private DataSet dataSet;
    private List<String> list;
    private Map<String, String> map;
    private String[] params;

    public UITemplate(String templateText) {
        super();
        initNodes(templateText);
    }

    public UITemplate(Class<?> class1, String id) {
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
        compressNodes(nodes, UIIfNode.StartFlag, UIIfNode.EndFlag, (text) -> new UIIfNode(text));
        compressNodes(nodes, UIMapNode.StartFlag, UIMapNode.EndFlag, (text) -> new UIMapNode(text));
        compressNodes(nodes, UIListNode.StartFlag, UIListNode.EndFlag, (text) -> new UIListNode(text));
        compressNodes(nodes, UIDatasetNode.StartFlag, UIDatasetNode.EndFlag, (text) -> new UIDatasetNode(text));
    }

    public UITemplate setArray(String... params) {
        this.params = params;
        return this;
    }

    public UITemplate setList(List<String> list) {
        this.list = list;
        return this;
    }

    public UITemplate setMap(Map<String, String> map) {
        this.map = map;
        return this;
    }

    public UITemplate setDataRow(DataRow dataRow) {
        this.dataRow = dataRow;
        return this;
    }

    public UITemplate setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public String html() {
        var sb = new StringBuffer();
        for (var node : this.nodes) {
            if (node instanceof UIListNode items)
                sb.append(items.getValue(list));
            else if (node instanceof UIMapNode items)
                sb.append(items.getValue(map));
            else if (node instanceof UIDatasetNode items)
                sb.append(items.getValue(dataSet));
            else if (node instanceof UIIfNode iif)
                sb.append(iif.getValue(dataRow));
            else if (node instanceof UIValueNode item) {
                var field = item.getText();
                if (Utils.isNumeric(field)) {
                    if (params != null) {
                        var index = Integer.parseInt(item.getText());
                        if (index >= 0 && index < params.length) {
                            sb.append(params[index]);
                        } else {
                            log.error("not find index: {}", item.getText());
                            sb.append(node.getSourceText());
                        }
                    } else {
                        sb.append(item.getSourceText());
                    }
                } else if (dataRow != null) {
                    if (dataRow.exists(field)) {
                        sb.append(dataRow.getString(field));
                    } else {
                        log.error("not find field: {}", field);
                        sb.append(node.getSourceText());
                    }
                } else
                    sb.append(item.getSourceText());
            } else
                sb.append(node.getText());

        }
        return sb.toString();
    }

    private void compressNodes(ArrayList<UISsrNodeImpl> nodes, String startFlag, String endFlag,
            SupperForeachImpl supper) {
        // 先复制到 temp 变量中
        var temp = new ArrayList<UISsrNodeImpl>();
        temp.addAll(nodes);
        nodes.clear();
        // 开始归集
        UIForeachNode container = null;
        for (var node : temp) {
            if (node instanceof UIValueNode item) {
                if (item.getText().startsWith(startFlag)) {
                    container = supper.createObject(item.getText());
                    nodes.add(container);
                    continue;
                } else if (item.getText().startsWith(endFlag)) {
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

    private ArrayList<UISsrNodeImpl> createNodes(String templateText) {
        var list = new ArrayList<UISsrNodeImpl>();
        int start, end;
        var line = templateText;
        while (line.length() > 0) {
            if ((start = line.indexOf("${")) > -1 && (end = line.indexOf("}")) > -1) {
                list.add(new UITextNode(line.substring(0, start)));
                list.add(new UIValueNode(line.substring(start + 2, end)));
                line = line.substring(end + 1, line.length());
            } else {
                list.add(new UITextNode(line));
                break;
            }
        }
        return list;
    }

    public List<UISsrNodeImpl> getNodes() {
        return nodes;
    }

}
