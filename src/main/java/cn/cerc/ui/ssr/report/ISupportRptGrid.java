package cn.cerc.ui.ssr.report;

import com.itextpdf.text.Paragraph;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.ssr.core.SummaryTypeEnum;

public interface ISupportRptGrid {

    void title(String title);

    void field(String field);

    void width(int width);

    int width();

    RptCellAlign align();

    default Paragraph outputTotal(DataSet dataSet) {
        return new Paragraph();
    }

    default SummaryTypeEnum summaryType() {
        return SummaryTypeEnum.无;
    }

}
