package cn.cerc.ui.plugins;

import java.util.List;

import cn.cerc.db.core.IHandle;

public interface PluginsImpl {

    /**
     * 设置其所需要配置的主类，若不通过则返回为假
     * 
     * @param owner 主类对象
     */
    default boolean setOwner(Object owner) {
        if (owner instanceof IHandle handle) {
            List<String> supportCorpList = getSupportCorpList();
            if (supportCorpList == null)
                return true;
            return supportCorpList.contains(handle.getCorpNo());
        } else
            return false;
    }

    List<String> getSupportCorpList();

}
