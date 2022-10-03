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
     * @param active 是否输出
     * @return 返回视图管理器
     */
    Object setActive(boolean active);

    /**
     * 设置视图管理器的视图处理器
     * 
     * @param dataStyle 视图管理器
     * @return 返回视图管理器自身
     */
    Object setDataStyle(UIDataStyleImpl dataStyle);

    /**
     * 
     * @return 返回视图处理器，可为null
     */
    UIDataStyleImpl dataStyle();
}
