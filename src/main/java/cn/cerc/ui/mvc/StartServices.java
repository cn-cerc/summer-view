package cn.cerc.ui.mvc;

import java.io.IOException;
import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.db.core.DataException;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Handle;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.ISession;
import cn.cerc.db.core.ServiceException;
import cn.cerc.db.core.Utils;
import cn.cerc.db.core.Variant;
import cn.cerc.db.other.RecordFilter;
import cn.cerc.mis.core.AppClient;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.IService;
import cn.cerc.mis.core.ServiceState;
import cn.cerc.mis.log.JayunLogParser;
import cn.cerc.mis.security.Permission;
import cn.cerc.mis.security.SecurityStopException;
import cn.cerc.ui.SummerUI;

public class StartServices extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 2699818753661287159L;
    private static final Logger log = LoggerFactory.getLogger(StartServices.class);
    private static final ClassConfig config = new ClassConfig(StartServices.class, SummerUI.ID);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        DataSet dataOut = new DataSet();
        String text = request.getParameter("dataIn");
        DataSet dataIn = new DataSet().setJson(text);
        String key = request.getPathInfo().substring(1);
        response.setContentType("application/json;charset=utf-8");
        // 处理跨域问题
        String allow = config.getProperty("access-control-allow-origin");
        if (!Utils.isEmpty(allow))
            response.addHeader("access-control-allow-origin", allow);
        log.debug("dataIn {}", text);

        if (Utils.isEmpty(key)) {
            dataOut.setMessage("远程服务不能为空");
            response.getWriter().write(dataOut.json());
            return;
        }

        // 获取token
        String token = request.getParameter(ISession.TOKEN);// sid
        if (Utils.isEmpty(token))
            token = request.getParameter("token");// token

        // 使用token登录，并获取用户资料与授权数据
        ISession session = Application.getBean(ISession.class);
        session.setProperty(ISession.REQUEST, request);
        boolean loadToken = session.loadToken(token);

        // 获取服务执行函数
        IHandle handle = new Handle(session);
        Variant function = new Variant("execute").setKey(key);
        IService service;
        try {
            service = Application.getService(handle, key, function);
        } catch (ClassNotFoundException e) {
            String clientIP = AppClient.getClientIP(request);
            log.error("clientIP {}, token{} , service {}, dataIn {}, error {}", clientIP, token, key, text,
                    e.getMessage(), e);
            dataOut.setState(ServiceState.NOT_FIND_SERVICE).setMessage("无效的访问请求，远程服务不存在");
            response.getWriter().write(dataOut.json());
            return;
        }

        // 取出执行服务需要的权限
        Class<? extends IService> clazz = service.getClass();
        Permission permission = clazz.getAnnotation(Permission.class);
        String value = Permission.USERS;
        if (permission != null)
            value = permission.value();

        // 非访客权限且令牌失效
        if (!Utils.isEmpty(token) && !Permission.GUEST.equals(value) && !loadToken) {
            dataOut.setState(ServiceState.TOKEN_INVALID).setMessage("当前会话已失效，请重退出新登录");
            response.getWriter().write(dataOut.json());
            return;
        }

        try {
            dataOut = service._call(handle, dataIn, function);
            if (dataOut == null) {
                dataOut = new DataSet();
                dataOut.setError().setMessage("远程服务没有响应");
            }
        } catch (RuntimeException | IllegalAccessException | InvocationTargetException | ServiceException
                | DataException e) {
            Throwable throwable = e.getCause() != null ? e.getCause() : e;
            String clientIP = AppClient.getClientIP(request);
            String message = String.format("clientIP %s, token %s, service %s, corpNo %s, dataIn %s, message %s",
                    clientIP, token, function.key(), handle.getCorpNo(), dataIn.json(), throwable.getMessage());

            if (e instanceof SecurityStopException)
                JayunLogParser.warn(clazz, throwable, message);// 用户权限不足，记入警告类日志
            else
                JayunLogParser.error(clazz, throwable, message);
            log.info("{}", message, throwable);
            dataOut.setError().setMessage(throwable.getMessage());
        }
        // 数据过滤后返回
        response.getWriter().write(RecordFilter.execute(dataIn, dataOut).json());
    }

}
