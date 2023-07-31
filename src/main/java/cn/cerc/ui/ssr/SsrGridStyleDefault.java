package cn.cerc.ui.ssr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SsrGridStyleDefault implements SsrGridStyleImpl {

    private List<String> items = new ArrayList<>();

    @Override
    public SupplierBlockImpl getIt(String title, int fieldWidth) {
        items.add(title);
        return grid -> {
            String headTitle = "head." + title;
            String bodyTitle = "body." + title;
            var ssr = grid.addBlock(headTitle, "<th width=${_width}>序</th>");
            ssr.setId(headTitle);
            ssr.display(1);
            ssr.toMap("_width", "" + fieldWidth);
            ssr = grid.addBlock(bodyTitle, "<td align='center'>${dataset.rec}</td>");
            ssr.setId(bodyTitle);
            ssr.display(1);
            return ssr;
        };
    }

    @Override
    public SupplierBlockImpl getOpera(int fieldWidth) {
        String title = "操作";
        items.add(title);
        return grid -> {
            String headTitle = "head." + title;
            String bodyTitle = "body." + title;
            var ssr = grid.addBlock(headTitle, String.format("<th width=${_width}>%s</th>", title));
            ssr.toMap("_width", "" + fieldWidth);
            ssr.setId(headTitle);
            ssr.display(1);
            ssr = grid.addBlock(bodyTitle, "<td><a href='${callback(url)}'>内容</a></td>");
            ssr.setId(bodyTitle);
            ssr.display(1);
            return ssr;
        };
    }

    @Override
    public SupplierBlockImpl getDate(String title, String field) {
        return getString(title, field, 5, "center");
    }

    public SupplierBlockImpl getDouble(String title, String field) {
        return getString(title, field, 4, "right");
    }

    @Override
    public SupplierBlockImpl getOption(String title, String field, int fieldWidth, Map<String, String> map) {
        items.add(title);
        return grid -> {
            String headTitle = "head." + title;
            String bodyTitle = "body." + title;
            var head = grid.addBlock(headTitle, String.format("<th width=${_width}>%s</th>", title));
            head.toMap("_width", "" + fieldWidth);
            head.setId(headTitle);
            head.display(1);
            var body = grid.addBlock(bodyTitle, String.format("""
                    <td>${if readonly}${map.begin}${if map.key==%s}${map.value}${endif}${map.end}${else}
                    <select>
                    ${map.begin}
                    <option value ="${map.key}" ${if map.key==%s}selected${endif}>${map.value}</option>
                    ${map.end}
                    </select>
                    ${endif}</td>
                    """, field, field));
            body.option("readonly", "true");
            body.setId(bodyTitle);
            body.display(1);
            map.forEach((key, value) -> body.toMap(key, value));
            return body;
        };
    }

    @Override
    public SupplierBlockImpl getBoolean(String title, String field, int fieldWidth) {
        items.add(title);
        return grid -> {
            String headTitle = "head." + title;
            String bodyTitle = "body." + title;
            var ssr = grid.addBlock(headTitle, String.format("<th width=${_width}>%s</th>", title));
            ssr.toMap("_width", "" + fieldWidth);
            ssr.setId(headTitle);
            ssr.display(1);
            ssr = grid.addBlock(bodyTitle, String.format("""
                        <td>
                            <span><input type='checkbox' value='1' ${if %s}checked ${endif}/></span>
                        </td>
                    """, field));
            ssr.setId(bodyTitle);
            ssr.setId(bodyTitle);
            ssr.display(1);
            return ssr;
        };
    }

    public interface SupplierStringImpl extends SupplierBlockImpl {
        SupplierStringImpl url(Supplier<String> url);
    }

    @Override
    public SupplierStringImpl getString(String title, String field, int fieldWidth) {
        items.add(title);
        return new SupplierStringImpl() {
            private Supplier<String> url;

            @Override
            public SsrBlockImpl request(SsrComponentImpl grid) {
                String headTitle = "head." + title;
                String bodyTitle = "body." + title;
                var ssr = grid.addBlock(headTitle, String.format("<th width=${_width}>%s</th>", title));
                ssr.toMap("_width", "" + fieldWidth);
                ssr.setId(headTitle);
                ssr.display(1);
                ssr = grid.addBlock(bodyTitle, String.format(
                        "<td align='left'>${if _enabled_url}<a href='${callback(url)}'>${endif}${dataset.%s}${if _enabled_url}</a>${endif}</td>",
                        field));
                ssr.setId(bodyTitle);
                ssr.display(1);
                ssr.strict(false);
                if (url != null) {
                    ssr.option("_enabled_url", "1");
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

    private SupplierBlockImpl getString(String title, String field, int fieldWidth, String align) {
        return grid -> {
            String headTitle = "head." + title;
            String bodyTitle = "body." + title;
            var ssr = grid.addBlock(headTitle, String.format("<th width=${_width}>%s</th>", title));
            ssr.toMap("_width", "" + fieldWidth);
            ssr.setId(headTitle);
            ssr.display(1);
            ssr = grid.addBlock(bodyTitle, String.format("<td align='${align}'>${dataset.%s}</td>", field));
            ssr.option("align", align);
            ssr.setId(bodyTitle);
            ssr.display(1);
            return ssr;
        };
    }

    @Override
    public List<String> items() {
        return items;
    }
}
