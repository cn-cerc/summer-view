package cn.cerc.ui.ssr;

import java.util.function.Supplier;

public interface SupplierStringImpl extends SupplierCustomBlockImpl {

    default SupplierStringImpl url(Supplier<String> url) {
        block().option("_enabled_url", "1");
        block().onCallback("_url", url);
        return this;
    };

    default SupplierStringImpl readonly(boolean readonly) {
        block().option(SsrOptionImpl.Readonly, readonly ? "1" : "");
        return this;
    }
}
