package cn.cerc.ui.plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import cn.cerc.mis.client.ServiceSign;
import cn.cerc.mis.core.AbstractForm;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.IPage;

/**
 * 用于公司别客制化需求
 * 
 * @author ZhangGong
 *
 */
public class CorpPlugins {
    private static final Logger log = LoggerFactory.getLogger(CorpPlugins.class);
//    private static final Logger log = LoggerFactory.getLogger(CorpPlugins.class);

    /**
     * 判断当前公司别当前对象，是否存在插件，如FrmProduct_131001（必须继承IPlugins）
     *
     * @param owner        插件拥有者，一般为 form
     * @param requiredType IPlugins 的实现类
     * @return 帐套是否存在插件客制化
     */
    public static boolean exists(Object owner, Class<? extends IPlugins> requiredType) {
        ApplicationContext context = Application.getContext();
        if (context == null)
            return false;
        String customName = PluginsFactory.getCorpClassName(owner);
        if (customName == null) {
            return false;
        }
        String[] beans = context.getBeanNamesForType(requiredType);
        for (String item : beans) {
            if (item.equals(customName))
                return true;
        }
        return false;
    }

    /**
     * 取得调用者的函数名称
     * 
     * @return form的函数名称，如 execute append modify 等
     */
    protected final static String getSenderFuncCode() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[3];
        return e.getMethodName();
    }

    /**
     * 用于自定义 page 场景，或重定向到新的 form
     *
     * @param form AbstractForm 的实现类
     * @return 如返回 RedirectPage 对象
     */
    public final static IPage getRedirectPage(AbstractForm form) {
        IPlugins plugins = PluginsFactory.getPluginsByCorp(form, IPlugins.class);
        if (!(plugins instanceof IRedirectPage)) {
            return null;
        }
        return plugins != null ? ((IRedirectPage) plugins).getPage() : null;
    }

    @Deprecated
    public static String getService(AbstractForm form, String defaultService) {
        return getServiceSign(form, new ServiceSign(defaultService)).id();
    }

    /**
     * 用于自定义服务场影
     *
     * @param form           AbstractForm 的实现类
     * @param defaultService 默认服务名称
     * @return 返回自定义 service 或 defaultService
     */
    @Deprecated
    public static ServiceSign getServiceSign(AbstractForm form, ServiceSign defaultService) {
        IPlugins plugins = PluginsFactory.getPluginsByCorp(form, IPlugins.class);
        if (plugins == null)
            return defaultService;
        if (!(plugins instanceof IServiceDefine))
            return defaultService;
        String result = ((IServiceDefine) plugins).getService();
        if (result == null)
            return defaultService;
        if (defaultService.server() != null)
            log.error("{} 非普通服务，不支持客制化，需要修正！", defaultService.id());
        return new ServiceSign(result);
    }

    public static IPlugins getBean(AbstractForm form, Class<IPlugins> class1) {
        return PluginsFactory.getPluginsByCorp(form, class1);
    }

}
