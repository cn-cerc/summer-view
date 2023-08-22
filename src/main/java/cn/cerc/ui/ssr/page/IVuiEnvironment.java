package cn.cerc.ui.ssr.page;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.VuiComponent;

public interface IVuiEnvironment {
    /**
     * 从数据库读取页面属性
     * 
     * @return
     */
    String loadProperties();

    /**
     * 将页面属性存入到数据库
     * 
     * @param jsonProperties
     */
    void saveProperties(String jsonProperties);

    /**
     * 对指定的组件附加组件，一般用于客制化
     * 
     * @param ownerClass
     * @param customClass
     */
    void attachClass(Class<? extends VuiComponent> ownerClass, Class<? extends VuiComponent> customClass);

    Set<Class<? extends VuiComponent>> getAttachClass(Class<? extends VuiComponent> clazz);

    /**
     * 对指定的组件附件数据，一般用于客制化
     * 
     * @param ownerClass
     * @param sourceId
     * @param source
     */
    void attachData(Class<? extends VuiComponent> ownerClass, String sourceId, Object source);

    Map<String, Object> getAttachData(Class<? extends VuiComponent> clazz);

    /** 将组件类代码转为 beanId */
    String getBeanId(String classCode);

    /** 根据 beanId 取得相应的对象 */
    <T> Optional<T> getBean(String beanId, Class<T> requiredType);

    /** 画布所在的组件 */
    UIComponent getContent();

    Class<?> getSupportClass();

}
