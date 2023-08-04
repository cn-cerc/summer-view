package cn.cerc.ui.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.ServerConfig;
import cn.cerc.mis.core.Application;

public class PluginsFactory {
    private static final Logger log = LoggerFactory.getLogger(PluginsFactory.class);

    public static <T> List<T> get(Object owner, Class<T> requiredType) {
        Objects.requireNonNull(owner);
        var list = new ArrayList<T>();
        var item1 = getPluginsByCorp(owner, requiredType);
        if (item1 != null)
            list.add(item1);
        var item2 = getPluginsByIndustry(owner, requiredType);
        if (item2 != null)
            list.add(item2);
        return list;
    }

    /**
     * 返回当前公司别当前对象之之插件对象，如FrmProduct_131001（必须继承 IPlugins）
     *
     * @param owner        插件拥有者，一般为 form
     * @param requiredType IPlugins 的实现类
     * @return 对象本身
     */
    public static <T> T getPluginsByCorp(Object owner, Class<T> requiredType) {
        ApplicationContext context = Application.getContext();
        if (context == null)
            return null;
        String customName = getCorpClassName(owner);
        if (customName == null) {
            return null;
        }
        if (!context.containsBean(customName)) {
            return null;
        }
        T result = context.getBean(customName, requiredType);
        if (result != null) {
            // 要求必须继承IPlugins
            if (result instanceof IPlugins) {
                ((IPlugins) result).setOwner(owner);
            } else {
                log.warn("{} not supports IPlugins.", customName);
                return null;
            }
            if (result instanceof IHandle && owner instanceof IHandle) {
                ((IHandle) result).setSession(((IHandle) owner).getSession());
            }
        }
        return result;
    }

    protected static String getCorpClassName(Object owner) {
        String names[];
        if (owner instanceof Class)
            names = ((Class<?>) owner).getName().split("\\.");
        else
            names = owner.getClass().getName().split("\\.");
        String corpNo = null;
        if (owner instanceof IHandle)
            corpNo = ((IHandle) owner).getCorpNo();
        if (corpNo == null || "".equals(corpNo))
            return null;
        String target = names[names.length - 1] + "_" + corpNo;
        // 前两个字母都是大写，则不处理
        if (!target.substring(0, 2).toUpperCase().equals(target.substring(0, 2))) {
            target = target.substring(0, 1).toLowerCase() + target.substring(1, target.length());
        }
        return target;
    }

    public static <T> T getPluginsByIndustry(Object owner, Class<T> requiredType) {
        ApplicationContext context = Application.getContext();
        if (context == null)
            return null;
        String customName = getIndustryClassName(owner);
        if (customName == null)
            return null;
        if (!context.containsBean(customName))
            return null;
        var obj = context.getBean(customName);
        if (!requiredType.isAssignableFrom(obj.getClass()))
            return null;
        @SuppressWarnings("unchecked")
        T result = (T) obj;
        // 要求必须继承IPlugins
        if (result instanceof IPlugins) {
            ((IPlugins) result).setOwner(owner);
        } else {
            log.warn("{} not supports IPlugins.", customName);
            return null;
        }
        if (result instanceof IHandle && owner instanceof IHandle) {
            ((IHandle) result).setSession(((IHandle) owner).getSession());
        }
        return result;
    }

    protected static String getIndustryClassName(Object owner) {
        String names[];
        if (owner instanceof Class)
            names = ((Class<?>) owner).getName().split("\\.");
        else
            names = owner.getClass().getName().split("\\.");
        String industryCode = ServerConfig.getAppOriginal();
        String target = names[names.length - 1] + "_" + industryCode;
        // 前两个字母都是大写，则不处理
        if (!target.substring(0, 2).toUpperCase().equals(target.substring(0, 2))) {
            target = target.substring(0, 1).toLowerCase() + target.substring(1, target.length());
        }
        return target;
    }

}
