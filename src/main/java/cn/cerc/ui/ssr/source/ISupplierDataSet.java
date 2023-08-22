package cn.cerc.ui.ssr.source;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.ssr.excel.ISupportXls;
import cn.cerc.ui.ssr.page.ISupportCanvas;

public interface ISupplierDataSet extends ISupportCanvas, ISupportXls {

    DataSet dataSet();

}
