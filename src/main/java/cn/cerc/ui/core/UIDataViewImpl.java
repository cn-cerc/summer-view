package cn.cerc.ui.core;

import cn.cerc.db.core.DataSource;
import cn.cerc.ui.grid.UIDataStyleImpl;

public interface UIDataViewImpl extends DataSource {

    boolean active();

    Object setActive(boolean active);

    Object setDataStyle(UIDataStyleImpl dataStyle);

    UIDataStyleImpl dataStyle();
}
