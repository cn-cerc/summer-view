package cn.cerc.ui.core;

import cn.cerc.db.core.DataSetSourceImpl;
import cn.cerc.ui.grid.UIDataStyleImpl;

public interface UIDataViewImpl extends DataSetSourceImpl {

    boolean active();

    Object setActive(boolean active);

    Object setStyle(UIDataStyleImpl defaultStyle);

    UIDataStyleImpl defaultStyle();
}
