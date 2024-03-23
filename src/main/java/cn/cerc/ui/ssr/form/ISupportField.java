package cn.cerc.ui.ssr.form;

import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;

public interface ISupportField extends ISupplierBlock {
    SsrBlock block();

    public String fields();

    public ISupportField field(String field);

    public String title();

    public ISupportField title(String title);
}
