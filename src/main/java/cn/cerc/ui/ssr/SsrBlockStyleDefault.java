package cn.cerc.ui.ssr;

import java.util.Map;
import java.util.function.Supplier;

public class SsrBlockStyleDefault {

    public class SupplierString implements SupplierBlockImpl {
        private Supplier<String> url;
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
                        <label>%s</label>
                        ${if _enabled_%s}
                        <a href='${callback(%s)}'>${%s}</a>
                        ${else}
                        <span>${%s}</span>
                        ${endif}
                    </div>
                    """, title, field, field, field, field));
            block.strict(false);
            if (url != null) {
                block.option(String.format("_enabled_%s", field), "1");
                block.onCallback(field, url);
            }
            return block;
        }

        public SupplierBlockImpl url(Supplier<String> url) {
            this.url = url;
            return this;
        }
    }

    public SupplierString getString(String title, String field) {
        return new SupplierString(title, field);
    }

    public SupplierBlockImpl getOption(String title, String field, Map<String, String> map) {
        return chunk -> {
            var block = new SsrBlock(String.format("""
                    <div>
                        <label>%s</label>
                        <span>${map.begin}${if map.key==%s}${map.value}${endif}${map.end}</span>
                    </div>
                    """, title, field, field, field, field));
            block.setMap(map);
            block.strict(false);
            return block;
        };
    }

}
