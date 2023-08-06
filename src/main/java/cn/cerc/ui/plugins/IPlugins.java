package cn.cerc.ui.plugins;

/**
 * 为系统提供客制化支持<br/>
 * 请改使用PluginsImpl
 *
 */
@Deprecated
public interface IPlugins {

    /**
     * 在非单例模式下时，可以使用函数
     * 
     * @param owner
     */
    default void setOwner(Object owner) {

    }

    default String getFuncCode() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
//        for (StackTraceElement item : stacktrace) {
//            System.out.println(item.getMethodName());
//        }
        StackTraceElement e = stacktrace[3];
        return e.getMethodName();
    }
}
