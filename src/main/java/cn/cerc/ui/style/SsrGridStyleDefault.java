package cn.cerc.ui.style;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SsrGridStyleDefault implements SsrGridStyleImpl {

    private List<String> items = new ArrayList<>();

    @Override
    public SupplierTemplateImpl getIt(String title, int fieldWidth) {
        items.add(title);
        return grid -> {
            String headTitle = "head." + title;
            String bodyTitle = "body." + title;
            var ssr = grid.addTemplate(headTitle, "<th width=${_width}>序</th>");
            ssr.setId(headTitle);
            ssr.setOption("option", "1");
            ssr.toMap("_width", "" + fieldWidth);
            ssr = grid.addTemplate(bodyTitle, "<td align='center'>${dataset.rec}</td>");
            ssr.setId(bodyTitle);
            ssr.setOption("option", "1");
            return ssr;
        };
    }

    @Override
    public SupplierTemplateImpl getOpera(int fieldWidth) {
        String title = "操作";
        items.add(title);
        return grid -> {
            String headTitle = "head." + title;
            String bodyTitle = "body." + title;
            var ssr = grid.addTemplate(headTitle, String.format("<th width=${_width}>%s</th>", title));
            ssr.toMap("_width", "" + fieldWidth);
            ssr.setId(headTitle);
            ssr.setOption("option", "1");
            ssr = grid.addTemplate(bodyTitle, "<td><a href='${callback(url)}'>内容</a></td>");
            ssr.setId(bodyTitle);
            ssr.setOption("option", "1");
            return ssr;
        };
    }

    @Override
    public SupplierTemplateImpl getDate(String title, String field) {
        return getString(title, field, 5, "center");
    }

    public SupplierTemplateImpl getDouble(String title, String field) {
        return getString(title, field, 4, "right");
    }

    @Override
    public SupplierTemplateImpl getOption(String title, String field, int fieldWidth, Map<String, String> map) {
        items.add(title);
        return grid -> {
            String headTitle = "head." + title;
            String bodyTitle = "body." + title;
            var head = grid.addTemplate(headTitle, String.format("<th width=${_width}>%s</th>", title));
            head.toMap("_width", "" + fieldWidth);
            head.setId(headTitle);
            head.setOption("option", "1");
            var body = grid.addTemplate(bodyTitle, String.format("""
                    <td>${if readonly}${map.begin}${if map.key==%s}${map.value}${endif}${map.end}${else}
                    <select>
                    ${map.begin}
                    <option value ="${map.key}" ${if map.key==%s}selected${endif}>${map.value}</option>
                    ${map.end}
                    </select>
                    ${endif}</td>
                    """, field, field));
            body.setOption("readonly", "true");
            body.setId(bodyTitle);
            body.setOption("option", "1");
            map.forEach((key, value) -> body.toMap(key, value));
            return body;
        };
    }

    @Override
    public SupplierTemplateImpl getBoolean(String title, String field, int fieldWidth) {
        items.add(title);
        return grid -> {
            String headTitle = "head." + title;
            String bodyTitle = "body." + title;
            var ssr = grid.addTemplate(headTitle, String.format("<th width=${_width}>%s</th>", title));
            ssr.toMap("_width", "" + fieldWidth);
            ssr.setId(headTitle);
            ssr.setOption("option", "1");
            ssr = grid.addTemplate(bodyTitle, String.format("""
                        <td>
                            <span><input type='checkbox' value='1' ${if %s}checked ${endif}/></span>
                        </td>
                    """, field));
            ssr.setId(bodyTitle);
            ssr.setId(bodyTitle);
            ssr.setOption("option", "1");
            return ssr;
        };
    }

    public interface SupplierStringImpl extends SupplierTemplateImpl {
        SupplierStringImpl url(Supplier<String> url);
    }

    @Override
    public SupplierStringImpl getString(String title, String field, int fieldWidth) {
        items.add(title);
        return new SupplierStringImpl() {
            private Supplier<String> url;

            @Override
            public SsrTemplateImpl request(SsrComponentImpl grid) {
                String headTitle = "head." + title;
                String bodyTitle = "body." + title;
                var ssr = grid.addTemplate(headTitle, String.format("<th width=${_width}>%s</th>", title));
                ssr.toMap("_width", "" + fieldWidth);
                ssr.setId(headTitle);
                ssr.setOption("option", "1");
                ssr = grid.addTemplate(bodyTitle, String.format(
                        "<td align='left'>${if _enabled_url}<a href='${callback(url)}'>${endif}${dataset.%s}${if _enabled_url}</a>${endif}</td>",
                        field));
                ssr.setId(bodyTitle);
                ssr.setOption("option", "1");
                ssr.setStrict(false);
                if (url != null) {
                    ssr.setOption("_enabled_url", "1");
                    ssr.onCallback(field, url);
                }
                return ssr;
            }

            @Override
            public SupplierStringImpl url(Supplier<String> url) {
                this.url = url;
                return this;
            }
        };
    }

    private SupplierTemplateImpl getString(String title, String field, int fieldWidth, String align) {
        return grid -> {
            String headTitle = "head." + title;
            String bodyTitle = "body." + title;
            var ssr = grid.addTemplate(headTitle, String.format("<th width=${_width}>%s</th>", title));
            ssr.toMap("_width", "" + fieldWidth);
            ssr.setId(headTitle);
            ssr.setOption("option", "1");
            ssr = grid.addTemplate(bodyTitle, String.format("<td align='${align}'>${dataset.%s}</td>", field));
            ssr.setOption("align", align);
            ssr.setId(bodyTitle);
            ssr.setOption("option", "1");
            return ssr;
        };
    }

    @Override
    public List<String> items() {
        return items;
    }
}
