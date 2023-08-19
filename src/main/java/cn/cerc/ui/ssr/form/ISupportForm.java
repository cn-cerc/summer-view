package cn.cerc.ui.ssr.form;

import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;

public interface ISupportForm extends ISupplierBlock {
    SsrBlock block();

    public String fields();

    public ISupportForm field(String field);

    public String title();

    public ISupportForm title(String title);
}
