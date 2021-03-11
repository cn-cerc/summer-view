package cn.cerc.mis.core;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.cerc.core.ClassConfig;
import cn.cerc.core.ClassResource;
import cn.cerc.core.IHandle;
import cn.cerc.core.LanguageResource;
import cn.cerc.core.SupportHandle;
import cn.cerc.db.core.IAppConfig;
import cn.cerc.db.core.ServerConfig;
import cn.cerc.mis.config.ApplicationConfig;
import cn.cerc.mis.language.Language;
import cn.cerc.mvc.SummerMVC;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {
    private static final ClassResource res = new ClassResource(Application.class, SummerMVC.ID);
    private static final ClassConfig config = new ClassConfig(Application.class, SummerMVC.ID);
    // tomcat JSESSION.ID
    public static final String sessionId = "sessionId";
    // token id
    public static final String TOKEN = "ID";
    // user id
    public static final String userId = "UserID";
    public static final String userCode = "UserCode";
    public static final String userName = "UserName";
    public static final String roleCode = "RoleCode";
    public static final String bookNo = "BookNo";
    public static final String deviceLanguage = "language";
    // 签核代理用户列表，代理多个用户以半角逗号隔开
    public static final String ProxyUsers = "ProxyUsers";
    // 客户端代码
    public static final String clientIP = "clientIP";
    // 本地会话登录时间
    public static final String loginTime = "loginTime";
    // 浏览器通用客户设备Id
    public static final String webclient = "webclient";

    // 默认界面语言版本
    // FIXME: 2019/12/7 此处应改为从函数，并从配置文件中读取默认的语言类型
    public static final String App_Language = getAppLanguage(); // 可选：cn/en

    // 各类应用配置代码
    public static final String PATH_FORMS = "application.pathForms";
    public static final String PATH_SERVICES = "application.pathServices";
    public static final String FORM_WELCOME = "application.formWelcome";
    public static final String FORM_DEFAULT = "application.formDefault";
    public static final String FORM_LOGOUT = "application.formLogout";
    public static final String FORM_VERIFY_DEVICE = "application.formVerifyDevice";
    public static final String JSPFILE_LOGIN = "application.jspLoginFile";

    private static ApplicationContext context;
    private static String staticPath;

    static {
        staticPath = config.getString("app.static.path", "");
    }

    public static ApplicationContext getContext() {
        return context;
    }

    private static String getAppLanguage() {
        return LanguageResource.appLanguage;
    }

    public static void setContext(ApplicationContext applicationContext) {
        if (context != applicationContext) {
            if (context == null) {
            } else {
                log.warn("applicationContext overload!");
            }
            context = applicationContext;
        }
    }

    public static ApplicationContext get(ServletContext servletContext) {
        setContext(WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext));
        return context;
    }

    public static ApplicationContext get(ServletRequest request) {
        setContext(WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext()));
        return context;
    }

    public static <T> T getBean(String beanId, Class<T> requiredType) {
        return context.getBean(beanId, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType, String... beans) {
        for (String key : beans) {
            if (!context.containsBean(key)) {
                continue;
            }
            return context.getBean(key, requiredType);
        }
        return null;
    }

    public static <T> T getBean(Class<T> requiredType) {
        String[] items = requiredType.getName().split("\\.");
        String itemId = items[items.length - 1];

        String beanId;
        if (itemId.substring(0, 2).toUpperCase().equals(itemId.substring(0, 2))) {
            beanId = itemId;
        } else {
            beanId = itemId.substring(0, 1).toLowerCase() + itemId.substring(1);
        }

        return context.getBean(beanId, requiredType);
    }

    public static IHandle getHandle() {
        return getBean(IHandle.class, "AppHandle", "handle", "handleDefault");
    }

    /**
     * 请改为直接使用ClassResource，参考AppConfigDefault
     * 
     * @return
     */
    @Deprecated
    public static IAppConfig getAppConfig() {
        return getBean(IAppConfig.class, "AppConfig", "appConfig", "appConfigDefault");
    }

    @Deprecated // 请改使用 getBean
    public static <T> T get(IHandle handle, Class<T> requiredType) {
        return getBean(handle, requiredType);
    }

    public static <T> T getBean(IHandle handle, Class<T> requiredType) {
        T bean = getBean(requiredType);
        if (bean != null && handle != null) {
            if (bean instanceof SupportHandle) {
                ((SupportHandle) bean).init(handle);
            }
        }
        return bean;
    }

    public static IService getService(IHandle handle, String serviceCode) {
        IService bean = context.getBean(serviceCode, IService.class);
        if (bean != null && handle != null) {
            bean.init(handle);
        }
        return bean;
    }

    public static IPassport getPassport(IHandle handle) {
        IPassport bean = getBean(IPassport.class, "passport", "passportDefault");
        if (bean != null && handle != null) {
            bean.setHandle(handle);
        }
        return bean;
    }

    public static IForm getForm(HttpServletRequest req, HttpServletResponse resp, String formId) {
        if (formId == null || "".equals(formId) || "service".equals(formId)) {
            return null;
        }

        setContext(WebApplicationContextUtils.getRequiredWebApplicationContext(req.getServletContext()));

        if (!context.containsBean(formId)) {
            throw new RuntimeException(String.format("form %s not find!", formId));
        }

        IForm form = context.getBean(formId, IForm.class);
        if (form != null) {
            form.setRequest(req);
            form.setResponse(resp);
        }

        return form;
    }

    public static String getLanguage() {
        String lang = ServerConfig.getInstance().getProperty(deviceLanguage);
        if (lang == null || "".equals(lang) || App_Language.equals(lang)) {
            return App_Language;
        } else if (Language.en_US.equals(lang)) {
            return lang;
        } else {
            throw new RuntimeException("not support language: " + lang);
        }
    }

    public static String getFormView(HttpServletRequest req, HttpServletResponse resp, String formId, String funcCode,
            String... pathVariables) {
        // 设置登录开关
        req.setAttribute("logon", false);

        // 验证菜单是否启停
        IFormFilter formFilter = Application.getBean(IFormFilter.class, "AppFormFilter");
        if (formFilter != null) {
            try {
                if (formFilter.doFilter(resp, formId, funcCode)) {
                    return null;
                }
            } catch (IOException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }

        IHandle handle = null;
        try {
            IForm form = Application.getForm(req, resp, formId);
            if (form == null) {
                outputErrorPage(req, resp, new RuntimeException("error servlet:" + req.getServletPath()));
                return null;
            }

            // 设备讯息
            AppClient client = new AppClient();
            client.setRequest(req);
            req.setAttribute("_showMenu_", !AppClient.ee.equals(client.getDevice()));
            form.setClient(client);

            // 建立数据库资源
            handle = Application.getHandle();
            handle.setProperty(Application.sessionId, req.getSession().getId());
            handle.setProperty(Application.deviceLanguage, client.getLanguage());
            req.setAttribute("myappHandle", handle);
            form.setHandle(handle);

            // 传递路径变量
            form.setPathVariables(pathVariables);

            // 进行安全检查，若未登录则显示登录对话框
            if (form.logon()) {
                return form.getView(funcCode);
            }

            IAppLogin appLogin = Application.getBean(IAppLogin.class, "appLogin", "appLoginDefault");
            appLogin.init(form);

            String loginJspFile = null;
            // 若页面有传递用户帐号，则强制重新校验
            if (form.getRequest().getParameter("login_usr") != null) {
                // 检查服务器的角色状态，如果是从服务器，则允许登录
                if (ApplicationConfig.isReplica())
                    throw new RuntimeException(res.getString(1, "当前服务不支持登录，请返回首页重新登录"));

                String login_usr = req.getParameter("login_usr");
                String login_pwd = req.getParameter("login_pwd");
                loginJspFile = appLogin.checkLogin(login_usr, login_pwd);
            } else {
                // 检查session或url中是否存在sid
                loginJspFile = appLogin.checkToken(client.getToken());
            }
            if (loginJspFile != null) {
                return loginJspFile;
            } else {
                return form.getView(funcCode);
            }
        } catch (Exception e) {
            outputErrorPage(req, resp, e);
            return null;
        } finally {
            if (handle != null) {
                handle.close();
            }
        }
    }

    public static void outputView(HttpServletRequest request, HttpServletResponse response, String url)
            throws IOException, ServletException {
        if (url == null)
            return;

        if (url.startsWith("redirect:")) {
            String redirect = url.substring(9);
            redirect = response.encodeRedirectURL(redirect);
            response.sendRedirect(redirect);
            return;
        }

        // 输出jsp文件
        String jspFile = String.format("/WEB-INF/%s/%s", config.getString(Application.PATH_FORMS, "forms"), url);
        request.getServletContext().getRequestDispatcher(jspFile).forward(request, response);
    }

    public static void outputErrorPage(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        Throwable err = e.getCause();
        if (err == null) {
            err = e;
        }
        IAppErrorPage errorPage = Application.getBean(IAppErrorPage.class, "appErrorPage", "appErrorPageDefault");
        if (errorPage != null) {
            String result = errorPage.getErrorPage(request, response, err);
            if (result != null) {
                String url = String.format("/WEB-INF/%s/%s", config.getString(Application.PATH_FORMS, "forms"), result);
                try {
                    request.getServletContext().getRequestDispatcher(url).forward(request, response);
                } catch (ServletException | IOException e1) {
                    log.error(e1.getMessage());
                    e1.printStackTrace();
                }
            }
        } else {
            log.warn("not define bean: errorPage");
            log.error(err.getMessage());
            err.printStackTrace();
        }
    }

    /**
     * 获得 token 的值
     * 
     * @param handle
     * @return
     */
    public static String getToken(IHandle handle) {
        return (String) handle.getProperty(Application.TOKEN);
    }

    public static String getStaticPath() {
        return staticPath;
    }

}