package cn.cerc.ui.core;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.grid.UIDataStyleImpl;

public interface UIDataViewImpl {
    
    DataSet dataSet();

    boolean active();

    Object setActive(boolean active);

    Object setDataStyle(UIDataStyleImpl dataStyle);

    UIDataStyleImpl dataStyle();
}
