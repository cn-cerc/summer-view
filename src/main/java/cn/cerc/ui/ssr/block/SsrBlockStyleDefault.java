package cn.cerc.ui.ssr.block;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.editor.ISsrBoard;

public class SsrBlockStyleDefault {

    public class SupplierString implements ISupplierBlock {
        private Supplier<String> url;
        private SsrBlock block = new SsrBlock();
        private String title;
        private String field;

        public SupplierString(String title, String field) {
            block.option("_select", "");
            this.title = title;
            this.field = field;
        }

        @Override
        public SsrBlock request(ISsrBoard owner) {
            block.text(String.format(
                    """
                            <div style='flex: ${_ratio};'>
                                <label for='%s'>%s</label>
                                ${if _enabled_url}<a id='%s' href='${callback(url)}'>${else}<span id='%s'>${endif}${if _select}${map.begin}${if map.key==%s}${map.value}${endif}${map.end}${else}${%s}${endif}${if _enabled_url}</a>${else}</span>${endif}
                                </div>""",
                    field, title, field, field, field, field));
            block.option("_ratio", "1");
            block.option("_enabled_url", url != null ? "1" : "");
            if (url != null)
                block.onCallback("url", url);

            return block;
        }

        public SupplierString url(Supplier<String> url) {
            this.url = url;
            return this;
        }

        public SupplierString toMap(String key, String value) {
            block.toMap(key, value);
            block.option("_select", "1");
            return this;
        }

        public SupplierString toMap(Map<String, String> map) {
            block.toMap(map);
            block.option("_select", "1");
            return this;
        }
    }

    public class SupplierNumber implements ISupplierBlock {
        private Supplier<String> url;
        private SsrBlock block = new SsrBlock();
        private String title;
        private String field;

        public SupplierNumber(String title, String field) {
            block.option("_select", "");
            this.title = title;
            this.field = field;
        }

        @Override
        public SsrBlock request(ISsrBoard owner) {
            block.text(String.format(
                    """
                            <div style='flex: ${_ratio};'>
                                <label for='%s'>%s</label>
                                ${if _enabled_url}<a id='%s' href='${callback(url)}'>${else}<span id='%s'>${endif}${if _select}${list.begin}${if list.index==%s}${list.value}${endif}${list.end}${else}${%s}${endif}${if _enabled_url}</a>${else}</span>${endif}
                                </div>
                            """,
                    field, title, field, field, field, field));
            block.option("_ratio", "1");
            block.option("_enabled_url", url != null ? "1" : "");
            if (url != null)
                block.onCallback("url", url);

            return block;
        }

        public SupplierNumber url(Supplier<String> url) {
            this.url = url;
            return this;
        }

        public SupplierNumber toList(String... values) {
            block.toList(values);
            block.option("_select", "1");
            return this;
        }

        public SupplierNumber toList(List<String> list) {
            block.toList(list);
            block.option("_select", "1");
            return this;
        }

        public SupplierNumber toList(Enum<?>[] enums) {
            block.toList(enums);
            block.option("_select", "1");
            return this;
        }
    }

    public SupplierString getString(String title, String field) {
        return new SupplierString(title, field);
    }

    public SupplierString getRowString(String title, String field) {
        return new SupplierString(title + "：", field);
    }

    public SupplierNumber getNumber(String title, String field) {
        return new SupplierNumber(title, field);
    }

    public SupplierNumber getRowNumber(String title, String field) {
        return new SupplierNumber(title + "：", field);
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

    /** 请改用getString */
    @Deprecated
    public ISupplierBlock getOption(String title, String field, Enum<?>[] enums) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Enum<?> item : enums) {
            map.put(String.valueOf(item.ordinal()), item.name());
        }
        return getOption(title, field, map);
    }

    /** 请改用getString */
    @Deprecated
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
