package cn.cerc.ui.mvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Handle;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.ISession;
import cn.cerc.db.core.ServiceException;
import cn.cerc.db.core.Utils;
import cn.cerc.db.core.Variant;
import cn.cerc.db.other.RecordFilter;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.DataValidateException;
import cn.cerc.mis.core.IService;
import cn.cerc.mis.core.ServiceState;
import cn.cerc.ui.SummerUI;

public class StartServices extends HttpServlet {
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
        String uri = request.getRequestURI();
        log.debug(uri);
        request.setCharacterEncoding("UTF-8");

        DataSet dataOut = new DataSet();
//        Variant variant = new Variant();
//        if (!AppClient.createCookie(request, response, variant)) {
//            StringBuilder builder = new StringBuilder(variant.getString());
//            builder.append(request.getRequestURI());
//            request.getParameterMap().forEach((key, value) -> {
//                builder.append(key);
//                Stream.of(value).forEach(builder::append);
//            });
//            String md5 = MD5.get(builder.toString());
//            String key = MemoryBuffer.buildKey(SystemBuffer.User.Frequency, md5);
//            try (Jedis jedis = JedisFactory.getJedis()) {
//                if (jedis.setnx(key, "1") == 1) {
//                    jedis.expire(key, 1);
//                } else {
//                    log.error("key {}, origin {}", key, builder);
//                    dataOut.setMessage(String.format("对不起您操作太快了，服务器忙不过来 %s", uri));
//                    response.getWriter().write(dataOut.toString());
//                    return;
//                }
//            }
//        }

        String text = request.getParameter("dataIn");
        DataSet dataIn = new DataSet().setJson(text);
        String service = request.getPathInfo().substring(1);
        response.setContentType("application/json;charset=utf-8");
        // 处理跨域问题
        String allow = config.getProperty("access-control-allow-origin");
        if (!Utils.isEmpty(allow))
            response.addHeader("access-control-allow-origin", allow);
        log.debug("dataIn {}", text);

        if (Utils.isEmpty(service)) {
            dataOut.setMessage("service is null.");
            response.getWriter().write(dataOut.toString());
            return;
        }

        // 执行指定函数
        try {
            ISession session = Application.getSession();
            session.setProperty(ISession.REQUEST, request);
            // 获取token
            String token = request.getParameter(ISession.TOKEN);
            if (Utils.isEmpty(token))
                token = request.getParameter("token");
            // 使用token登录，并获取用户资料与授权数据
            session.loadToken(token);
            // token失效则直接返回
            if ((Integer) session.getProperty(token) == ServiceState.TOKEN_INVALID) {
                dataOut.setState(ServiceState.TOKEN_INVALID).setMessage("token is invalid, please login again.");
                response.getWriter().write(RecordFilter.execute(dataIn, dataOut).toString());
                return;
            }

            IHandle handle = new Handle(session);
            Variant function = new Variant("execute").setKey(service);
            IService bean = Application.getService(handle, service, function);
            dataOut = bean._call(handle, dataIn, function);
            if (dataOut == null)
                dataOut = new DataSet().setMessage("service return empty");
            response.getWriter().write(RecordFilter.execute(dataIn, dataOut).toString());
        } catch (DataValidateException e) {
            dataOut.setState(ServiceState.ERROR);
            dataOut.setMessage(e.getMessage());
            response.getWriter().write(dataOut.toString());
        } catch (ClassNotFoundException e) {
            dataOut.setState(ServiceState.NOT_FIND_SERVICE);
            dataOut.setMessage(e.getMessage());
            response.getWriter().write(dataOut.toString());
        } catch (ServiceException e) {
            Throwable err = e.getCause() != null ? e.getCause() : e;
            log.error(err.getMessage(), err);
            dataOut.setState(ServiceState.ERROR).setMessage(err.getMessage());
            e.printStackTrace();
            response.getWriter().write(dataOut.toString());
        }
    }

}
