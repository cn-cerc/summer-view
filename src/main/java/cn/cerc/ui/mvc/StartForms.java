package cn.cerc.ui.mvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
import cn.cerc.db.core.Utils;
import cn.cerc.mis.config.AppStaticFileDefault;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.FormFactory;
import cn.cerc.mis.core.FormSign;
import cn.cerc.mis.core.IErrorPage;

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
        if (StringUtils.countMatches(uri, "/") < 2 && !uri.contains("favicon.ico")) {
            String redirect = String.format("/public/%s", Application.getConfig().getWelcomePage());
            redirect = resp.encodeRedirectURL(redirect);
            resp.sendRedirect(redirect);
            return;
        }

        // 1???????????????????????????
        if (AppStaticFileDefault.getInstance().isStaticFile(uri)) {
            // ???????????????????????????????????????????????????????????????
            // TODO ???????????????????????????jar?????????????????????
            if (uri.contains("imgZoom")) {
                chain.doFilter(req, resp);
                return;
            }

            /*
             * 1??? ????????? getPathForms ???????????????????????????forms?????????????????????????????????????????????????????? AppConfig
             * 2???????????????????????????????????????????????????????????????????????????????????? 3?????????/ /131001/images/systeminstall-pc.png ->
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

        if (uri.contains("static/") || uri.contains("service/") || uri.contains("services/") || uri.contains("task/")
                || uri.contains("docs/")) {
            chain.doFilter(req, resp);
            return;
        }

        ApplicationContext context = WebApplicationContextUtils
                .getRequiredWebApplicationContext(req.getServletContext());
        Application.setContext(context);

        ISession session = context.getBean(ISession.class);
        session.setRequest(req);
        session.setResponse(resp);

        // 2?????????Url??????
        String childCode = getRequestCode(req);
        if (childCode == null) {
            IErrorPage error = context.getBean(IErrorPage.class);
            error.output(req, resp, new RuntimeException("??????????????????" + req.getServletPath()));
            return;
        }

        FormFactory factory = context.getBean(FormFactory.class);
        IHandle handle = new Handle(session);
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
