package cn.cerc.ui.style;

import java.util.Map;
import java.util.function.Consumer;

public class SsrDefaultGridStyle {

    public Consumer<SsrComponentImpl> getIt(int fieldWidth) {
        return grid -> {
            var field = "#it";
            grid.addTemplate("head." + field, "<th width=${_width}>序</th>");
            grid.addTemplate("body." + field, "<td>${dataset.rec}</td>");
            grid.onGetHtml("head.#it", ssr -> ssr.toMap("_width", "" + fieldWidth));
            grid.addField(field);
        };
    }

    public Consumer<SsrComponentImpl> getOpera(String field, int fieldWidth, String url) {
        return grid -> {
            var flag = url.indexOf("?") == -1 ? "?" : "&";
            grid.addTemplate("head._opera", "<th width=${_width}>操作</th>");
            grid.addTemplate("body._opera",
                    String.format("<td><a href='%s%s%s=${%s}'>内容</a></td>", url, flag, field, field));
            grid.onGetHtml("head._opera", ssr -> ssr.toMap("_width", "" + fieldWidth));
            grid.addField("_opera");
        };
    }

    public Consumer<SsrComponentImpl> getString(String field, String fieldName, int fieldWidth) {
        return grid -> {
            grid.addTemplate("head." + field, String.format("<th width=${_width}>%s</th>", fieldName));
            grid.addTemplate("body." + field, String.format("<td>${dataset.%s}</td>", field));
            grid.onGetHtml("head." + field, ssr -> ssr.toMap("_width", "" + fieldWidth));
            grid.addField(field);
        };
    }

    public Consumer<SsrComponentImpl> getBoolean(String field, String fieldName, int fieldWidth, String fieldTitle) {
        return grid -> {
            grid.addTemplate("head." + field, String.format("<th width=${_width}>%s</th>", fieldTitle));
            grid.addTemplate("body." + field, String
                    .format("<td><input type='checkbox' value='${dataset.%s}'>%s</input></td>", field, fieldName));
            grid.onGetHtml("head." + field, ssr -> ssr.toMap("_width", "" + fieldWidth));
            grid.addField(field);
        };
    }

    public Consumer<SsrComponentImpl> getOption(String field, String fieldName, int fieldWidth,
            Map<String, String> map) {
        return grid -> {
            grid.onGetHtml("head." + field, ssr -> ssr.toMap("_width", "" + fieldWidth));
            grid.onGetHtml("body." + field, ssr -> map.forEach((key, value) -> ssr.toMap(key, value)));
            grid.addTemplate("head." + field, String.format("<th width=${_width}>%s</th>", fieldName));
            grid.addTemplate("body." + field, String.format("""
                    <select>
                    ${map.begin}
                    <option value ="${map.key}" ${if map.key==%s}selected${endif}>${map.value}</option>
                    ${map.end}
                    </select>
                    """, field));
            grid.addField(field);
        };
    }
}
