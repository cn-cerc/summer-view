package cn.cerc.ui.mvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.core.ClassConfig;
import cn.cerc.core.DataSet;
import cn.cerc.core.ISession;
import cn.cerc.core.KeyValue;
import cn.cerc.core.Utils;
import cn.cerc.db.core.Handle;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.other.RecordFilter;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.DataValidateException;
import cn.cerc.mis.core.IService;
import cn.cerc.mis.core.ServiceException;
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
        String uri = request.getRequestURI();
        log.debug(uri);
        request.setCharacterEncoding("UTF-8");
        String text = request.getParameter("dataIn");
        DataSet dataIn = new DataSet().setJson(text);
        String service = request.getPathInfo().substring(1);
        response.setContentType("application/json;charset=utf-8");
        // 处理跨域问题
        String allow = config.getProperty("access-control-allow-origin");
        if (!Utils.isEmpty(allow))
            response.addHeader("access-control-allow-origin", allow);
        log.debug("dataIn {}", text);

        DataSet dataOut = new DataSet();
        if (Utils.isEmpty(service)) {
            dataOut.setMessage("service is null.");
            response.getWriter().write(dataOut.toString());
            return;
        }

        // 执行指定函数
        try {
            ISession session = Application.getSession();
            session.setProperty(ISession.REQUEST, request);
            session.setProperty(Application.SessionId, request.getSession().getId());
            // 获取token
            String token = request.getParameter(ISession.TOKEN);
            if (Utils.isEmpty(token))
                token = request.getParameter("token");
            // 使用token登录，并获取用户资料与授权数据
            session.loadToken(token);

            IHandle handle = new Handle(session);
            KeyValue function = new KeyValue("execute").setKey(service);
            IService bean = Application.getService(handle, service, function);
            dataOut = bean._call(handle, dataIn, function);
            if (dataOut == null)
                dataOut = new DataSet().setMessage("service return empty");
            response.getWriter().write(RecordFilter.execute(dataIn, dataOut).toString());
        } catch (DataValidateException e) {
            dataOut.setMessage(e.getMessage());
        } catch (ClassNotFoundException e) {
            dataOut.setMessage(e.getMessage()).setState(ServiceState.NOT_FIND_SERVICE);
            response.getWriter().write(dataOut.toString());
        } catch (ServiceException e) {
            Throwable err = e.getCause() != null ? e.getCause() : e;
            log.error(err.getMessage(), err);
            dataOut.setState(ServiceState.ERROR).setMessage(err.getMessage());
            response.getWriter().write(dataOut.toString());
            e.printStackTrace();
        }
    }

}
