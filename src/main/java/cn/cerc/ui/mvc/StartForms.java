package cn.cerc.ui.mvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.cerc.db.core.Handle;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.ISession;
import cn.cerc.db.core.MD5;
import cn.cerc.db.core.Utils;
import cn.cerc.db.core.Variant;
import cn.cerc.db.redis.Redis;
import cn.cerc.mis.config.AppStaticFileDefault;
import cn.cerc.mis.core.AppClient;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.FormFactory;
import cn.cerc.mis.core.FormSign;
import cn.cerc.mis.core.IErrorPage;
import cn.cerc.mis.core.SystemBuffer;
import cn.cerc.mis.other.MemoryBuffer;

public class StartForms implements Filter {
    private static final Logger log = LoggerFactory.getLogger(StartForms.class);

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("{} init.", this.getClass().getName());
    }

    @Override
    public void destroy() {
        log.info("{} destroy.", this.getClass().getName());
    }

    public static final List<String> paths = new ArrayList<>();

    static {
        paths.add("static/");
        paths.add("service/");
        paths.add("services/");
        paths.add("services-std/");
        paths.add("services-obm/");
        paths.add("services-oem/");
        paths.add("services-odm/");
        paths.add("services-fpl/");
        paths.add("services-npl/");
        paths.add("services-csm/");
        paths.add("services-jt/");
        paths.add("task/");
        paths.add("docs/");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        log.debug("uri {}", uri);

        if (StringUtils.countMatches(uri, "/") == 1 && uri.split("/")[1].startsWith("i.")) {
            resp.sendRedirect("/public/install?qr=" + uri.split("/")[1].substring(2));
            return;
        }
        if (StringUtils.countMatches(uri, "/") == 1 && uri.split("/")[1].equalsIgnoreCase("i")) {
            resp.sendRedirect("/public/install");
            return;
        }

        // 1、静态文件直接输出
        if (AppStaticFileDefault.getInstance().isStaticFile(uri)) {
            // 静态资源允许跨域访问
            resp.setHeader("Access-Control-Allow-Origin", "*");
            if (uri.contains(".well-known/")) {
                request.getServletContext().getRequestDispatcher(uri).forward(request, response);
                return;
            }

            // 默认没有重定向，直接读取资源文件的默认路径
            // TODO 暂时按该方法放行（jar包的资源文件）
            if (uri.contains("imgZoom")) {
                chain.doFilter(req, resp);
                return;
            }

            /*
             * 1、 此处的 getPathForms 对应资源文件目录的forms，可自行定义成其他路径，注意配套更新 AppConfig
             * 2、截取当前的资源路径，将资源文件重定向到容器中的项目路径 3、例如/ /131001/images/systeminstall-pc.png ->
             * /forms/images/systeminstall-pc.png
             */
            log.debug("before {}", uri);

            int index = uri.indexOf("/", 2);
            if (index < 0) {
                request.getServletContext().getRequestDispatcher(uri).forward(request, response);
            } else {
                String source = "/" + Application.getConfig().getFormsPath() + uri.substring(index);
                source = Utils.decode(source, StandardCharsets.UTF_8.name());
                request.getServletContext().getRequestDispatcher(source).forward(request, response);
                log.debug("after  {}", source);
            }
            return;
        }

        if (StringUtils.countMatches(uri, "/") < 2 && !uri.contains("favicon.ico")) {
            String redirect = String.format("/public/%s", Application.getConfig().getWelcomePage());
            redirect = resp.encodeRedirectURL(redirect);
            resp.sendRedirect(redirect);
            return;
        }

        if (paths.stream().anyMatch(uri::contains)) {
            chain.doFilter(req, resp);
            return;
        }

        ApplicationContext context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(req.getServletContext());
        Application.setContext(context);

        ISession session = context.getBean(ISession.class);
        session.setRequest(req);
        session.setResponse(resp);

        // 2、处理Url请求
        String childCode = getRequestCode(req);
        if (childCode == null) {
            IErrorPage error = context.getBean(IErrorPage.class);
            error.output(req, resp, new RuntimeException("无效的请求：" + req.getServletPath()));
            return;
        }

        FormFactory factory = context.getBean(FormFactory.class);
        IHandle handle = new Handle(session);

        boolean existWhiteList = false;
        IFormWhiteListVerify whiteListVerify = Application.getBean(IFormWhiteListVerify.class);
        if (whiteListVerify != null)
            existWhiteList = whiteListVerify.exist(childCode);

        if ("POST".equalsIgnoreCase(req.getMethod()) && !existWhiteList) {
            Variant variant = new Variant();
            if (!AppClient.createCookie(req, resp, variant)) {
                StringBuilder builder = new StringBuilder(variant.getString());
                if (!uri.contains("WebDefault")) {
                    builder.append(uri);
                    req.getParameterMap().forEach((key, value) -> {
                        builder.append(key);
                        Stream.of(value).forEach(builder::append);
                    });

                    String md5 = MD5.get(builder.toString());
                    String key = MemoryBuffer.buildKey(SystemBuffer.User.Frequency, md5);
                    try (Redis jedis = new Redis()) {
                        if (jedis.setnx(key, "1") == 1) {
                            jedis.expire(key, 1);
                        } else {
                            log.error("key {}, origin {}", key, builder);
                            IErrorPage error = context.getBean(IErrorPage.class);
                            error.output(req, resp, new RuntimeException(String.format("对不起您操作太快了，服务器忙不过来 %s", uri)));
                            return;
                        }
                    }
                }
            }
        }

        FormSign sv = new FormSign(childCode);
        String viewId = factory.getView(handle, req, resp, sv.getId(), sv.getValue());
        factory.outputView(req, resp, viewId);
    }

    public static String getRequestCode(HttpServletRequest req) {
        String url = null;
        log.debug("servletPath {}", req.getServletPath());
        String[] args = req.getServletPath().split("/");
        if (args.length == 2 || args.length == 3) {
            if ("".equals(args[0]) && !"".equals(args[1])) {
                if (args.length == 3) {
                    url = args[2];
                } else {
                    String token = (String) req.getAttribute(ISession.TOKEN);
                    if (token != null && !"".equals(token)) {
                        url = Application.getConfig().getDefaultPage();
                    } else {
                        url = Application.getConfig().getWelcomePage();
                    }
                }
            }
        }
        return url;
    }

}
