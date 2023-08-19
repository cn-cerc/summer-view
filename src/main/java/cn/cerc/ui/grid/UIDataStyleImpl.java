package cn.cerc.ui.grid;

import java.util.HashMap;

import cn.cerc.db.core.DataSetSource;
import cn.cerc.db.core.FieldMeta;

/**
 * 视图样式处理器
 * 
 * @author ZhangGong
 *
 */
public interface UIDataStyleImpl extends DataSetSource {

    /**
     * 于样式处理器中注册一个字段，若不存在，则自动于dataSet.fields中增加
     * 
     * @param fieldCode 字段代码
     * @return 字段样式数据对象
     */
    FieldStyleDefine addField(String fieldCode);

    FieldStyleDefine getFieldStyle(String fieldCode);

    /**
     * 
     * @return 返回所有已注册的字段
     */
    HashMap<String, FieldStyleDefine> fields();

    /**
     * 设置fieldMeta的onGetText事件函数
     * 
     * @param fieldMeta 字段定义数据
     * @return 是否设置成功
     */
    boolean setDefault(FieldMeta fieldMeta);

    /**
     * 是否在表格环境中显示
     * 
     * @param grid 若为表格环境，则设置为true
     */
    Object setInGrid(boolean grid);

}
