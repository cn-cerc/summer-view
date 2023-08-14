package cn.cerc.ui.ssr;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import cn.cerc.mis.core.Application;
import cn.cerc.ui.fields.ImageConfigImpl;

public class SsrGridStyleDefault implements SsrGridStyleImpl {

    private List<String> items = new ArrayList<>();
    private ImageConfigImpl imageConfig;

    public SupplierBlockImpl getIt() {
        return getIt("序", 2);
    }

    protected String getImage(String imgSrc) {
        if (imageConfig == null)
            imageConfig = Application.getBean(ImageConfigImpl.class);
        return imageConfig == null ? imgSrc : imageConfig.getCommonFile(imgSrc);
    }

    @Override
    public SupplierBlockImpl getIt(String title, int fieldWidth) {
        items.add(title);
        return grid -> {
            String headTitle = "head." + title;
            String bodyTitle = "body." + title;
            var ssr = grid.addBlock(headTitle, "<th style='width: ${_width}em'>序</th>");
            ssr.id(headTitle);
            ssr.display(1);
            ssr.toMap("_width", "" + fieldWidth);
            ssr = grid.addBlock(bodyTitle, "<td align='center' role='_it_'>${dataset.rec}</td>");
            ssr.id(bodyTitle);
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
            var ssr1 = grid.addBlock(headTitle, String.format("""
                    <th style='width: ${_width}em'>
                    ${if templateId}
                        <a href="javascript:showSsrConfigDialog('${templateId}')">
                            <img src="%s" style="width: 1rem;" />
                        </a>
                    ${else}
                    %s
                    ${endif}
                    </th>
                    """, getImage("images/icon/templateConfig_hover.png"), title));
            ssr1.toMap("_width", "" + fieldWidth);
            ssr1.option("templateId", "");
            ssr1.id(headTitle);

            var ssr2 = grid.addBlock(bodyTitle,
                    "<td align='center' role='_opera_'><a href='${callback(url)}'>内容</a></td>");
            ssr2.id(bodyTitle);
            ssr2.display(1);
            return ssr2;
        };
    }

    @Override
    public SupplierBlockImpl getDate(String title, String field) {
        return getString(title, field, 5, "center");
    }

    public SupplierBlockImpl getDatetime(String title, String field) {
        return getString(title, field, 10, "center");
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
            var head = grid.addBlock(headTitle, String.format("<th style='width: ${_width}em'>%s</th>", title));
            head.toMap("_width", "" + fieldWidth);
            head.id(headTitle);
            head.display(1);
            var body = grid.addBlock(bodyTitle, String.format("""
                    <td role='%s'>${if readonly}${map.begin}${if map.key==%s}${map.value}${endif}${map.end}${else}
                    <select>
                    ${map.begin}
                    <option value ="${map.key}" ${if map.key==%s}selected${endif}>${map.value}</option>
                    ${map.end}
                    </select>
                    ${endif}</td>
                    """, field, field, field));
            body.option("readonly", "true");
            body.id(bodyTitle);
            body.display(1);
            map.forEach((key, value) -> body.toMap(key, value));
            return body;
        };
    }

    public SupplierBlockImpl getOption(String title, String field, int fieldWidth, Enum<?>[] enums) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Enum<?> item : enums) {
            map.put(String.valueOf(item.ordinal()), item.name());
        }
        return getOption(title, field, fieldWidth, map);
    }

    // TODO 要支持自定义 checkbox 的value
    @Override
    public SupplierBlockImpl getBoolean(String title, String field, int fieldWidth) {
        items.add(title);
        return grid -> {
            String headTitle = "head." + title;
            String bodyTitle = "body." + title;
            var ssr = grid.addBlock(headTitle, String.format("<th style='width: ${_width}em'>%s</th>", title));
            ssr.toMap("_width", "" + fieldWidth);
            ssr.id(headTitle);
            ssr.display(1);
            ssr = grid.addBlock(bodyTitle,
                    String.format(
                            """
                                        <td align='center' role='%s'>
                                            <span><input type='checkbox' name='checkBoxName' value='${if checkbox_value_}${checkbox_value_}${else}1${endif}' ${if %s}checked ${endif}/></span>
                                        </td>
                                    """,
                            field, field));
            ssr.id(bodyTitle);
            ssr.display(1).strict(false);
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
            private SsrBlock block = new SsrBlock();

            @Override
            public SsrBlockImpl request(SsrComponentImpl grid) {
                String headTitle = "head." + title;
                var ssr = grid.addBlock(headTitle, String.format("<th style='width: ${_width}em'>%s</th>", title));
                ssr.toMap("_width", "" + fieldWidth);
                ssr.id(headTitle);
                ssr.display(1);

                String bodyTitle = "body." + title;
                grid.addBlock(bodyTitle, block.templateText(String.format(
                        "<td align='left' role='%s'>${if _enabled_url}<a href='${callback(url)}'>${endif}${dataset.%s}${if _enabled_url}</a>${endif}</td>",
                        field, field)));
                block.id(bodyTitle);
                block.display(1);
                block.strict(false);
                return block;
            }

            @Override
            public SupplierStringImpl url(Supplier<String> url) {
                block.option("_enabled_url", "1");
                block.onCallback("url", url);
                return this;
            }
        };
    }

    public SupplierBlockImpl getString(String title, String field, int fieldWidth, String align) {
        return grid -> {
            String headTitle = "head." + title;
            String bodyTitle = "body." + title;
            var ssr = grid.addBlock(headTitle, String.format("<th style='width: ${_width}em'>%s</th>", title));
            ssr.toMap("_width", "" + fieldWidth);
            ssr.id(headTitle);
            ssr.display(1);
            ssr = grid.addBlock(bodyTitle,
                    String.format("<td align='${align}' role='%s'>${dataset.%s}</td>", field, field));
            ssr.option("align", align);
            ssr.id(bodyTitle);
            ssr.display(1);
            return ssr;
        };
    }

    @Override
    public List<String> items() {
        return items;
    }

}
