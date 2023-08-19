package cn.cerc.ui.ssr.grid;

import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;

public interface ISupportGrid extends ISupplierBlock {
    SsrBlock block();

    public String title();

    public ISupportGrid title(String title);

    public String field();

    public ISupportGrid field(String field);

    public int width();

    public ISupportGrid width(int width);

}
