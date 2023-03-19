package cn.cerc.ui.core;

import java.util.Optional;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSetSource;
import cn.cerc.ui.grid.UIDataStyleImpl;

/**
 * 视图输出管理器
 * 
 * @author ZhangGong
 *
 */
public interface UIDataViewImpl extends DataSetSource {

    boolean active();

    /**
     * 
     * @return 返回视图处理器，可为null
     */
    UIDataStyleImpl dataStyle();

    default Optional<DataRow> currentRow() {
        return Optional.ofNullable(this.getDataSet().map(ds -> ds.current()).orElse(null));
    }

}
