package cn.cerc.ui.ssr.core;

import java.util.List;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.IHandle;

/**
 * 从数据库取得模版配置文件
 * 
 * @author 张弓 2023/8/3
 *
 */
public interface ISsrTemplateConfig {

    List<String> getFields(IHandle handle, DataSet dataSet);

}
