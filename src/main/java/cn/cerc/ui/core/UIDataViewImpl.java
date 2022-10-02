package cn.cerc.ui.core;

import cn.cerc.db.core.DataSetSourceImpl;
import cn.cerc.ui.grid.UIFieldStyleImpl;

public interface UIDataViewImpl extends DataSetSourceImpl {

    boolean active();

    Object setActive(boolean active);

    Object setViewStyle(UIFieldStyleImpl defaultStyle);

    UIFieldStyleImpl viewStyle();
}
