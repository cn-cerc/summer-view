package cn.cerc.ui.ssr.grid;

import cn.cerc.ui.ssr.ISsrBlock;
import cn.cerc.ui.ssr.ISupplierBlock;

public interface ISupportGrid extends ISupplierBlock {
    ISsrBlock block();

    public String title();

    public ISupportGrid title(String title);

    public String field();

    public ISupportGrid field(String field);

    public int width();

    public ISupportGrid width(int width);

}
