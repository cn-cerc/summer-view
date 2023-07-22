package cn.cerc.ui.style;

import java.util.Map;
import java.util.function.Consumer;

public class SsrDefaultGridStyle implements SsrGridStyleImpl {

    @Override
    public Consumer<SsrComponentImpl> getIt(String title, int fieldWidth) {
        return grid -> {
            grid.addTemplate("head." + title, "<th width=${_width}>序</th>");
            grid.addTemplate("body." + title, "<td>${dataset.rec}</td>");
            grid.onGetHtml("head." + title, ssr -> ssr.toMap("_width", "" + fieldWidth));
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getOpera(String title, String field, int fieldWidth, String url) {
        return grid -> {
            var flag = url.indexOf("?") == -1 ? "?" : "&";
            grid.addTemplate("head." + title, "<th width=${_width}>操作</th>");
            grid.addTemplate("body." + title,
                    String.format("<td><a href='%s%s%s=${%s}'>内容</a></td>", url, flag, field, field));
            grid.onGetHtml("head." + title, ssr -> ssr.toMap("_width", "" + fieldWidth));
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getString(String title, String field, int fieldWidth) {
        return grid -> {
            grid.addTemplate("head." + title, String.format("<th width=${_width}>%s</th>", title));
            grid.addTemplate("body." + title, String.format("<td>${dataset.%s}</td>", field));
            grid.onGetHtml("head." + title, ssr -> ssr.toMap("_width", "" + fieldWidth));
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getBoolean(String title, String field, int fieldWidth, String labelText) {
        return grid -> {
            grid.addTemplate("head." + title, String.format("<th width=${_width}>%s</th>", labelText));
            grid.addTemplate("body." + title,
                    String.format("<td><input type='checkbox' value='${dataset.%s}'>%s</input></td>", field, title));
            grid.onGetHtml("head." + title, ssr -> ssr.toMap("_width", "" + fieldWidth));
        };
    }

    @Override
    public Consumer<SsrComponentImpl> getOption(String title, String field, int fieldWidth, Map<String, String> map) {
        return grid -> {
            grid.onGetHtml("head." + title, ssr -> ssr.toMap("_width", "" + fieldWidth));
            grid.onGetHtml("body." + title, ssr -> map.forEach((key, value) -> ssr.toMap(key, value)));
            grid.addTemplate("head." + title, String.format("<th width=${_width}>%s</th>", title));
            grid.addTemplate("body." + title, String.format("""
                    <select>
                    ${map.begin}
                    <option value ="${map.key}" ${if map.key==%s}selected${endif}>${map.value}</option>
                    ${map.end}
                    </select>
                    """, field));
        };
    }
}
