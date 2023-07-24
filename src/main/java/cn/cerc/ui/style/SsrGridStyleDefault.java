package cn.cerc.ui.style;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SsrGridStyleDefault implements SsrGridStyleImpl {

    private List<String> items = new ArrayList<>();

    @Override
    public Consumer<SsrComponentImpl> getIt(String title, int fieldWidth) {
        items.add(title);
        return grid -> {
            var ssr = grid.addTemplate("head." + title, "<th width=${_width}>序</th>");
            ssr.toMap("_width", "" + fieldWidth);
            grid.addTemplate("body." + title, "<td>${dataset.rec}</td>");
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getOpera(String title, String field, int fieldWidth, String url) {
        items.add(title);
        return grid -> {
            var ssr = grid.addTemplate("head." + title, "<th width=${_width}>操作</th>");
            ssr.toMap("_width", "" + fieldWidth);
            var flag = url.indexOf("?") == -1 ? "?" : "&";
            grid.addTemplate("body." + title,
                    String.format("<td><a href='%s%s%s=${%s}'>内容</a></td>", url, flag, field, field));
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getString(String title, String field, int fieldWidth) {
        return grid -> {
            var ssr = grid.addTemplate("head." + title, String.format("<th width=${_width}>%s</th>", title));
            ssr.toMap("_width", "" + fieldWidth);
            grid.addTemplate("body." + title, String.format("<td>${dataset.%s}</td>", field));
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getBoolean(String title, String field, int fieldWidth, String labelText) {
        items.add(title);
        return grid -> {
            var ssr = grid.addTemplate("head." + title, String.format("<th width=${_width}>%s</th>", labelText));
            ssr.toMap("_width", "" + fieldWidth);
            grid.addTemplate("body." + title,
                    String.format("<td><input type='checkbox' value='${dataset.%s}'>%s</input></td>", field, title));
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getOption(String title, String field, int fieldWidth, Map<String, String> map) {
        items.add(title);
        return grid -> {
            var head = grid.addTemplate("head." + title, String.format("<th width=${_width}>%s</th>", title));
            head.toMap("_width", "" + fieldWidth);
            var body = grid.addTemplate("body." + title, String.format("""
                    <select>
                    ${map.begin}
                    <option value ="${map.key}" ${if map.key==%s}selected${endif}>${map.value}</option>
                    ${map.end}
                    </select>
                    """, field));
            map.forEach((key, value) -> body.toMap(key, value));
        };
    }

    public List<String> items() {
        return items;
    }
}
