package cn.cerc.ui.core;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSource;
import cn.cerc.db.core.DataSet;
import cn.cerc.ui.grid.UIDataStyleImpl;

public interface UIDataViewImpl extends DataSource {

    DataSet dataSet();

    Object setDataSet(DataSet dataSet);

    @Override
    default DataRow current() {
        return dataSet().current();
    }

    @Override
    default boolean isReadonly() {
        return dataSet().readonly();
    }

    boolean active();

    Object setActive(boolean active);

    Object setDataStyle(UIDataStyleImpl dataStyle);

    UIDataStyleImpl dataStyle();
}
