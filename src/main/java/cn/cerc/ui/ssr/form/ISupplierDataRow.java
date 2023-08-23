package cn.cerc.ui.ssr.form;

import cn.cerc.db.core.DataRow;
import cn.cerc.ui.ssr.page.ISupportCanvas;

/**
 * 为 UISsrForm 提供数据源
 *
 */
public interface ISupplierDataRow extends ISupportCanvas {

    DataRow dataRow();

}
