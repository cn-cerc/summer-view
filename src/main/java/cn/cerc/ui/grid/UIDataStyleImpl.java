package cn.cerc.ui.grid;

import java.util.HashMap;

import cn.cerc.db.core.DataSource;
import cn.cerc.db.core.FieldMeta;

/**
 * 视图样式处理器
 * 
 * @author ZhangGong
 *
 */
public interface UIDataStyleImpl extends DataSource {

    /**
     * 于样式处理器中注册一个字段，若不存在，则自动于dataSet.fields中增加
     * 
     * @param fieldCode 字段代码
     * @return 字段样式数据对象
     */
    FieldStyleData addField(String fieldCode);

    /**
     * 
     * @return 返回所有已注册的字段
     */
    HashMap<String, FieldStyleData> fields();

    /**
     * 设置fieldMeta的onGetText事件函数
     * 
     * @param fieldMeta 字段定义数据
     * @return 是否设置成功
     */
    boolean setDefault(FieldMeta fieldMeta);

}
