package cn.cerc.ui.ssr.block;

import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;

public interface ISupportBlock extends ISupplierBlock {
    SsrBlock block();

    String title();

    ISupportBlock title(String title);

    default String field() {
        return "";
    }

    default ISupportBlock field(String field) {
        return this;
    }
}
