package cn.cerc.ui.style;

import java.util.Map;

public class SsrDefaultGridStyle {

    private UISsrGrid grid;

    public SsrDefaultGridStyle(UISsrGrid grid) {
        this.grid = grid;
    }

    public SsrGridColumn getIt(int fieldWidth) {
        grid.addGetHead("#it", ssr -> ssr.toMap("_width", "" + fieldWidth));
        return new SsrGridColumn("#it", "<th width=${_width}>序</th>", "<td>${dataset.rec}</td>");
    }

    public SsrGridColumn getOpera(String fieldCode, int fieldWidth, String url) {
        grid.addGetHead("_opera", ssr -> ssr.toMap("_width", "" + fieldWidth));
        var flag = url.indexOf("?") == -1 ? "?" : "&";
        return new SsrGridColumn("_opera", "<th width=${_width}>操作</th>",
                String.format("<td><a href='%s%s%s=${%s}'>内容</a></td>", url, flag, fieldCode, fieldCode));
    }

    public SsrGridColumn getString(String fieldCode, String fieldName, int fieldWidth) {
        grid.addGetHead(fieldCode, ssr -> ssr.toMap("_width", "" + fieldWidth));
        return new SsrGridColumn(fieldCode, String.format("<th width=${_width}>%s</th>", fieldName),
                String.format("<td>${dataset.%s}</td>", fieldCode));
    }

    public SsrGridColumn getBoolean(String fieldCode, String fieldName, int fieldWidth, String fieldTitle) {
        grid.addGetHead(fieldCode, ssr -> ssr.toMap("_width", "" + fieldWidth));
        return new SsrGridColumn(fieldCode, String.format("<th width=${_width}>%s</th>", fieldTitle), String
                .format("<td><input type='checkbox' value='${dataset.%s}'>%s</input></td>", fieldCode, fieldName));
    }

    public SsrGridColumn getOption(String fieldCode, String fieldName, int fieldWidth, Map<String, String> map) {
        grid.addGetHead(fieldCode, ssr -> ssr.toMap("_width", "" + fieldWidth));
        grid.addGetBody(fieldCode, ssr -> map.forEach((key, value) -> ssr.toMap(key, value)));
        var bodyText = String.format("""
                <select>
                ${map.begin}
                <option value ="${map.key}" ${if map.key==%s}selected${endif}>${map.value}</option>
                ${map.end}
                </select>
                """, fieldCode);
        return new SsrGridColumn(fieldCode, String.format("<th width=${_width}>%s</th>", fieldName), bodyText);
    }
}
