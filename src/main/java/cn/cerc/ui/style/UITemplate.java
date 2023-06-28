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

public class UITemplate {
    private static final Logger log = LoggerFactory.getLogger(UITemplate.class);
    private List<UISsrNodeImpl> nodes;

    public UITemplate(String text) {
        super();
        this.nodes = this.asNodes(text);
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
        this.nodes = this.asNodes(sb.toString());
    }

    public List<UISsrNodeImpl> getNodes() {
        return nodes;
    }

    public String decode(List<String> list) {
        var sb = new StringBuffer();
        var nodes = getForeachNodes(UIListNode.StartFlag, UIListNode.EndFlag, (text) -> new UIListNode(text));
        for (var node : nodes) {
            if (node instanceof UIListNode items)
                sb.append(items.getValue(list));
            else
                sb.append(node.getText());
        }
        return sb.toString();
    }

    public String decode(Map<String, String> map) {
        var sb = new StringBuffer();
        var nodes = getForeachNodes(UIMapNode.StartFlag, UIMapNode.EndFlag, (text) -> new UIMapNode(text));
        for (var node : nodes) {
            if (node instanceof UIMapNode items)
                sb.append(items.getValue(map));
            else
                sb.append(node.getText());
        }
        return sb.toString();
    }

    public String decode(DataRow row) {
        var sb = new StringBuffer();
        for (var node : this.nodes) {
            if (node instanceof UIValueNode item) {
                var field = item.getText();
                if (row.exists(field)) {
                    sb.append(row.getString(field));
                } else {
                    log.error("not find field: {}", field);
                    sb.append("${").append(field).append("}");
                }
            } else {
                sb.append(node.getText());
            }
        }
        return sb.toString();
    }

    public String decode(DataSet dataSet) {
        var sb = new StringBuffer();
        var nodes = getForeachNodes(UIDatasetNode.StartFlag, UIDatasetNode.EndFlag, (text) -> new UIDatasetNode(text));
        for (var node : nodes) {
            if (node instanceof UIDatasetNode items)
                sb.append(items.getValue(dataSet));
            else
                sb.append(node.getText());
        }
        return sb.toString();
    }

    private ArrayList<UISsrNodeImpl> getForeachNodes(String startFlag, String endFlag, SupperForeachImpl supper) {
        var result = new ArrayList<UISsrNodeImpl>();
        UIForeachNode start = null;
        for (var node : nodes) {
            if (node instanceof UIValueNode item) {
                if (startFlag.equals(item.getText())) {
                    start = supper.getObject(item.getText());
                    result.add(start);
                    continue;
                } else if (endFlag.equals(item.getText())) {
                    start = null;
                    continue;
                }
            }
            if (start != null) {
                start.addItem(node);
            } else {
                result.add(node);
            }
        }
        return result;
    }

    private List<UISsrNodeImpl> asNodes(String text) {
        var list = new ArrayList<UISsrNodeImpl>();
        int start, end;
        var line = text;
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

}
