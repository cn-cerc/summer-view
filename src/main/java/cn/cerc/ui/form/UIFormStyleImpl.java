package cn.cerc.ui.form;

import cn.cerc.db.core.DataSource;

public interface UIFormStyleImpl extends DataSource {
    FormStockStyle addStock(String caption);
}
