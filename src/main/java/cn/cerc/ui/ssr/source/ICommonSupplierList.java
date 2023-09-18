package cn.cerc.ui.ssr.source;

import cn.cerc.ui.ssr.chart.ISupportChart;
import cn.cerc.ui.ssr.excel.ISupportXls;
import cn.cerc.ui.ssr.page.ISupportCanvas;
import cn.cerc.ui.ssr.report.ISupportRpt;

public interface ICommonSupplierList extends ISupplierList, ISupportCanvas, ISupportXls, ISupportChart, ISupportRpt {

}
