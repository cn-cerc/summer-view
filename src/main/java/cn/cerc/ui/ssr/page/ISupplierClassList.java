package cn.cerc.ui.ssr.page;

import java.util.Map;
import java.util.Set;

import cn.cerc.ui.ssr.core.SsrComponent;

public interface ISupplierClassList {

    void attachClass(Class<? extends SsrComponent> ownerClass, Class<? extends SsrComponent> customClass);

    Set<Class<? extends SsrComponent>> getAttachClass(Class<? extends SsrComponent> clazz);

    void attachData(Class<? extends SsrComponent> ownerClass, String sourceId, Object source);

    Map<String, Object> getAttachData(Class<? extends SsrComponent> clazz);

}
