package cn.cerc.ui.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.context.ApplicationContext;

import cn.cerc.mis.core.Application;

public class PluginsFactory {
//    private static final Logger log = LoggerFactory.getLogger(PluginsFactory.class);

    /**
     * 请改使用 getPluginsList
     * 
     * @param <T>
     * @param owner
     * @param requiredType
     * @return
     */
    @Deprecated
    public static <T extends PluginsImpl> List<T> get(Object owner, Class<T> requiredType) {
        return getPluginsList(owner, requiredType);
    }

    public static <T extends PluginsImpl> List<T> getPluginsList(Object owner, Class<T> requiredType) {
        Objects.requireNonNull(owner);
        var list = new ArrayList<T>();

        ApplicationContext context = Application.getContext();
        if (context == null)
            return null;
        for (var beanId : context.getBeanNamesForType(requiredType)) {
            T bean = context.getBean(beanId, requiredType);
            if (bean.setOwner(owner))
                list.add(bean);
        }

        return list;
    }

}
