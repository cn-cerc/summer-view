package cn.cerc.ui.ssr.block;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.editor.ISsrBoard;

public class SsrBlockStyleDefault {

    @Deprecated
    public class SupplierString implements ISupportBlock {
        private Supplier<String> url;
        private SsrBlock block = new SsrBlock();
        private String title;
        private String field;

        public SupplierString(String title, String field) {
            this.title = title;
            this.field = field;
            init();
        }

        public SupplierString() {
            init();
        }

        private void init() {
            block.option("_select", "");
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

        @Override
        public SsrBlock block() {
            return block;
        }

        @Override
        public String title() {
            return title;
        }

        @Override
        public ISupportBlock title(String title) {
            this.title = title;
            return this;
        }

        @Override
        public String field() {
            return field;
        }

        @Override
        public ISupportBlock field(String field) {
            this.field = field;
            return this;
        }
    }

    /**
     * 请改用getString2
     */
    public SupplierString getString(String title, String field) {
        return new SupplierString(title, field);
    }

    /**
     * 请改用getRowString2
     */
    public SupplierString getRowString(String title, String field) {
        return new SupplierString(title + "：", field);
    }

    public BlockStringField getString2(String title, String field) {
        return new BlockStringField(title, field);
    }

    public BlockStringField getRowString2(String title, String field) {
        return new BlockStringField(title + "：", field);
    }

    public BlockNumberField getNumber(String title, String field) {
        return new BlockNumberField(title, field);
    }

    public BlockNumberField getRowNumber(String title, String field) {
        return new BlockNumberField(title + "：", field);
    }

    public BlockBooleanField getBoolean(String title, String field) {
        return new BlockBooleanField(title, field);
    }

    public BlockBooleanField getRowBoolean(String title, String field) {
        return new BlockBooleanField(title + "：", field);
    }

    public BlockItFIeld getIt() {
        return new BlockItFIeld();
    }

    public BlockCheckBoxField getCheckbox(String checkboxField, String checkboxValueField) {
        return new BlockCheckBoxField(checkboxField, checkboxValueField);
    }

    public BlockCheckBoxField getCheckbox(String checkboxField, Supplier<String> checkboxValue) {
        return new BlockCheckBoxField(checkboxField, checkboxValue);
    }

    public ISupplierBlock getCheckboxIt(String checkboxField, String checkboxValueField) {
        return chunk -> {
            var block = new SsrBlock(String.format("""
                    <div role='checkboxIt'>
                        <input type="checkbox" id="%s" name="%s" value="${%s}"/>
                        <span>${dataset.rec}</span>
                    </div>""", checkboxField, checkboxField, checkboxValueField));
            return block;
        };
    }

    public ISupplierBlock getCheckboxIt(String checkboxField, Supplier<String> checkboxValue) {
        return chunk -> {
            var block = new SsrBlock(String.format("""
                    <div role='checkboxIt'>
                        <input type="checkbox" id="%s" name="%s" value="${callback(%s)}"/>
                        <span>${dataset.rec}</span>
                    </div>""", checkboxField, checkboxField, checkboxField));
            block.onCallback(checkboxField, checkboxValue);
            return block;
        };
    }

    /**
     * 如果是value值为enum的下标的枚举请改为getNumber+toList方法，toList需要跟在getNumber之后
     * style.getNumber('title', 'field').toList(enum.values())
     * 如果value为非enum的下标的枚举请改为getString+toMap方法，toMap需要跟在getString之后
     * style.getString('title', 'field').toMap(Map.of('', ''))
     */
    @Deprecated
    public ISupplierBlock getOption(String title, String field, Enum<?>[] enums) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Enum<?> item : enums) {
            map.put(String.valueOf(item.ordinal()), item.name());
        }
        return getOption(title, field, map);
    }

    /** 请改用getString(需要将toMap的操作转移到getString之后) */
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
            block.toMap(map);
            return block;
        };
    }

    public ISupplierBlock getOpera(String field) {
        return new BlockOperaField(field);
    }

    public ISupplierBlock getOpera(Supplier<String> url) {
        return new BlockOperaField(url);
    }

}
