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

public class UITemplate {
    private ArrayList<String> template = new ArrayList<String>();

    public ArrayList<String> getTemplate() {
        return template;
    }

    public UITemplate() {

    }

    public UITemplate(Class<?> class1, String id) {
        var fileName = class1.getSimpleName() + "_" + id + ".html";
        var file = class1.getResourceAsStream(fileName);
        var list = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8));
        String line;
        boolean start = false;
        try {
            while ((line = list.readLine()) != null) {
                if ("<body>".equals(line))
                    start = true;
                else if ("</body>".equals(line))
                    break;
                else if (start)
                    template.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String decode(List<String> list) {
        var sb = new StringBuffer();
        // TODO
        return sb.toString();
    }

    private String decode(Map<String, String> map) {
        var sb = new StringBuffer();
        // TODO
        return sb.toString();
    }

    public String decode(DataRow row) {
        var sb = new StringBuffer();
        for (var line : template) {
            var start = line.indexOf("${");
            if (start > -1) {
                sb.append(line.substring(0, start));

                var str = line.substring(start + 2, line.length());
                var end = str.indexOf("}");
                var field = str.substring(0, end);

                if (row.exists(field)) {
                    sb.append(row.getString(field));
                } else {
                    System.out.println("not find field: " + field);
                }
                sb.append(str.substring(end + 1, str.length()));
            } else {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private String decode(DataSet dataSet) {
        var sb = new StringBuffer();
        // TODO
        return sb.toString();
    }

    public static void main(String[] args) {
        var template = new UITemplate();

        var list = template.getTemplate();
        list.clear();
        list.add("<div>");
        list.add("${list.begin>");
        list.add("<span>${list.item}</span>");
        list.add("${list.end}");
        list.add("</div>");
        System.out.println(template.decode(List.of("main", "beta")));

        list.clear();
        list.add("<div>");
        list.add("${map.begin}");
        list.add("<span>${map.key}:${map.value}</span>");
        list.add("${map.end}");
        list.add("</div>");
        System.out.println(template.decode(Map.of("code", "001", "name", "jason")));

        list.clear();
        list.add("<div>");
        list.add("<span>${Code_}</span>");
        list.add("</div>");
        System.out.println(template.decode(DataRow.of("Code_", "001")));

        list.clear();
        list.add("<div>");
        list.add("<table>");
        list.add("${dataset.begin}");
        list.add("<span>${Code_}</span>");
        list.add("${dataset.end}");
        list.add("</table>");
        list.add("</div>");

        var dataSet = new DataSet();
        dataSet.append().setValue("Code_", "001");
        dataSet.append().setValue("Code_", "002");
        System.out.println(template.decode(dataSet));

    }

}
