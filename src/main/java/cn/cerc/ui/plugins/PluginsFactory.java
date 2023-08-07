package cn.cerc.ui.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.context.ApplicationContext;

import cn.cerc.mis.core.Application;

public class PluginsFactory {
//    private static final Logger log = LoggerFactory.getLogger(PluginsFactory.class);

    public static <T extends PluginsImpl> Optional<T> getPluginsOne(Object owner, Class<T> requiredType) {
        var list = getPluginsList(owner, requiredType);
        if (list.isEmpty())
            return Optional.empty();
        else if (list.size() > 1) {
            List<String> items = new ArrayList<>();
            for (var plugins : list)
                items.add(plugins.getClass().getSimpleName());
            throw new RuntimeException(String.format("接口 %s 出现重复的实现类：%s", requiredType.getClass().getSimpleName(),
                    String.join(",", items)));
        }
        return Optional.of(list.get(0));
    }

    public static <T extends PluginsImpl> List<T> getPluginsList(Object owner, Class<T> requiredType) {
        Objects.requireNonNull(owner);
        var list = new ArrayList<T>();
        ApplicationContext context = Application.getContext();
        if (context != null) {
            for (var beanId : context.getBeanNamesForType(requiredType)) {
                T bean = context.getBean(beanId, requiredType);
                if (bean.setOwner(owner))
                    list.add(bean);
            }
        }
        return list;
    }

}
