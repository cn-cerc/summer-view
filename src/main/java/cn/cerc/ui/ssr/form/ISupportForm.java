package cn.cerc.ui.ssr.form;

import cn.cerc.ui.ssr.ISsrBlock;
import cn.cerc.ui.ssr.ISupplierBlock;

public interface ISupportForm extends ISupplierBlock {
    ISsrBlock block();

    public String fields();

    public ISupportForm field(String field);

    public String title();

    public ISupportForm title(String title);
}
