package cn.cerc.ui.ssr.grid;

import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SummaryTypeEnum;

public interface ISupportGrid extends ISupplierBlock {
    SsrBlock block();

    public String title();

    public ISupportGrid title(String title);

    public String field();

    public ISupportGrid field(String field);

    public int width();

    public ISupportGrid width(int width);

    default public SummaryTypeEnum summaryType() {
        return SummaryTypeEnum.无;
    }

    default void outputTotal(HtmlWriter html, DataSet dataSet) {
        html.print("<td></td>");
    }

}
