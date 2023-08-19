package cn.cerc.ui.ssr.block;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.editor.ISsrBoard;

public class SsrBlockStyleDefault {

    public class SupplierString implements ISupplierBlock {
        private Supplier<String> url;
        private String title;
        private String field;

        public SupplierString(String title, String field) {
            this.title = title;
            this.field = field;
        }

        @Override
        public SsrBlock request(ISsrBoard owner) {
            var block = new SsrBlock(String.format("""
                    <div style='flex: ${_ratio};'>
                        <label for='%s'>%s</label>
                        ${if _enabled_%s}
                            <a id='%s' href='${callback(%s)}'>${%s}</a>
                        ${else}
                            <span id='%s'>${%s}</span>
                        ${endif}
                    </div>
                    """, field, title, field, field, field, field, field, field));
            block.option("_ratio", "1");
            if (url != null) {
                block.option(String.format("_enabled_%s", field), "1");
                block.onCallback(field, url);
            } else
                block.option(String.format("_enabled_%s", field), "0");

            return block;
        }

        public SupplierString url(Supplier<String> url) {
            this.url = url;
            return this;
        }
    }

    public SupplierString getString(String title, String field) {
        return new SupplierString(title, field);
    }

    public SupplierString getRowString(String title, String field) {
        return new SupplierString(title + "：", field);
    }

    public ISupplierBlock getBoolean(String title, String field) {
        return chunk -> {
            var block = new SsrBlock(String.format("""
                    <div style='flex: ${_ratio};'>
                        <label for='%s'>%s</label>
                        <span id='%s'>
                            ${if %s}
                            是
                            ${else}
                            否
                            ${endif}
                        </span>
                    </div>""", field, title, field, field));
            block.option("_ratio", "1");
            return block;
        };
    }

    public ISupplierBlock getRowBoolean(String title, String field) {
        return getBoolean(title + "：", field);
    }

    public ISupplierBlock getIt() {
        return chunk -> {
            var block = new SsrBlock("""
                    <div role='gridIt'>
                        <span>${dataset.rec}</span>
                    </div>""");
            return block;
        };
    }

    public ISupplierBlock getCheckbox(String checkboxField, String checkboxValueField) {
        return chunk -> {
            var block = new SsrBlock(String.format("""
                    <div role='checkbox'>
                        <input type="checkbox" name="%s" value="${%s}"/>
                    </div>""", checkboxField, checkboxValueField));
            return block;
        };
    }

    public ISupplierBlock getCheckbox(String checkboxField, Supplier<String> checkboxValue) {
        return chunk -> {
            var block = new SsrBlock(String.format("""
                    <div role='checkbox'>
                        <input type="checkbox" name="%s" value="${callback(%s)}"/>
                    </div>""", checkboxField, checkboxField));
            block.onCallback(checkboxField, checkboxValue);
            return block;
        };
    }

    public ISupplierBlock getCheckboxIt(String checkboxField, String checkboxValueField) {
        return chunk -> {
            var block = new SsrBlock(String.format("""
                    <div role='checkboxIt'>
                        <input type="checkbox" name="%s" value="${%s}"/>
                        <span>${dataset.rec}</span>
                    </div>""", checkboxField, checkboxValueField));
            return block;
        };
    }

    public ISupplierBlock getCheckboxIt(String checkboxField, Supplier<String> checkboxValue) {
        return chunk -> {
            var block = new SsrBlock(String.format("""
                    <div role='checkboxIt'>
                        <input type="checkbox" name="%s" value="${callback(%s)}"/>
                        <span>${dataset.rec}</span>
                    </div>""", checkboxField, checkboxField));
            block.onCallback(checkboxField, checkboxValue);
            return block;
        };
    }

    public ISupplierBlock getOption(String title, String field, Enum<?>[] enums) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Enum<?> item : enums) {
            map.put(String.valueOf(item.ordinal()), item.name());
        }
        return getOption(title, field, map);
    }

    public ISupplierBlock getOption(String title, String field, Map<String, String> map) {
        return chunk -> {
            var block = new SsrBlock(String.format("""
                    <div style='flex: ${_ratio};'>
                        <label for='%s'>%s</label>
                        <span id='%s'>${map.begin}${if map.key==%s}${map.value}${endif}${map.end}</span>
                    </div>
                    """, field, title, field, field, field, field, field));
            block.option("_ratio", "1");
            block.setMap(map);
            return block;
        };
    }

    public ISupplierBlock getOpera(String field) {
        return chunk -> {
            var block = new SsrBlock(String.format("""
                    <div role='opera'>
                        <a href='${%s}'>内容</a>
                    </div>
                    """, field));
            return block;
        };
    }

    public ISupplierBlock getOpera(Supplier<String> url) {
        return chunk -> {
            var block = new SsrBlock("""
                    <div role='opera'>
                        <a href='${callback(href)}'>内容</a>
                    </div>
                    """);
            block.onCallback("href", url);
            return block;
        };
    }

}
