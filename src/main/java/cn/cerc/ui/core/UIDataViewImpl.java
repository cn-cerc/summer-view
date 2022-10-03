package cn.cerc.ui.core;

import cn.cerc.db.core.DataSource;
import cn.cerc.ui.grid.UIDataStyleImpl;

/**
 * 视图输出管理器
 * 
 * @author ZhangGong
 *
 */
public interface UIDataViewImpl extends DataSource {

    boolean active();

    /**
     * 
     * @return 返回视图处理器，可为null
     */
    UIDataStyleImpl dataStyle();
}
