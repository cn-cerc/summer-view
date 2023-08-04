package cn.cerc.ui.ssr;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SsrBlockStyleDefault {

    public class SupplierString implements SupplierBlockImpl {
        private Supplier<String> url;
        private Supplier<String> value;
        private String title;
        private String field;

        public SupplierString(String title, String field) {
            this.title = title;
            this.field = field;
        }

        @Override
        public SsrBlockImpl request(SsrComponentImpl owner) {
            var block = new SsrBlock(String.format("""
                    <div>
                        <label for='%s'>%s${if _hasColon}：${endif}</label>
                        ${if _enabled_%s}
                            <a id='%s' href='${callback(%s)}'>${%s}</a>
                        ${else}
                            <span id='%s'>${%s}</span>
                        ${endif}
                    </div>
                    """, field, title, field, field, field, field, field, field));
            block.option("_hasColon", "");
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

    public SupplierBlockImpl getOption(String title, String field, Enum<?>[] enums) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Enum<?> item : enums) {
            map.put(String.valueOf(item.ordinal()), item.name());
        }
        return getOption(title, field, map);
    }

    public SupplierBlockImpl getOption(String title, String field, Map<String, String> map) {
        return chunk -> {
            var block = new SsrBlock(String.format("""
                    <div>
                        <label for='%s'>%s</label>
                        <span id='%s'>${map.begin}${if map.key==%s}${map.value}${endif}${map.end}</span>
                    </div>
                    """, field, title, field, field, field, field, field));
            block.setMap(map);
            return block;
        };
    }

    public SupplierBlockImpl getOpera(String field) {
        return chunk -> {
            var block = new SsrBlock(String.format("""
                    <div role='opera'>
                        <a href='${%s}'>内容</a>
                    </div>
                    """, field));
            return block;
        };
    }

    public SupplierBlockImpl getOpera(Supplier<String> url) {
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
