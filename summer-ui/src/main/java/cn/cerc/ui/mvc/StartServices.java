package cn.cerc.ui.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.core.DataSet;
import cn.cerc.core.ISession;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.BasicHandle;
import cn.cerc.mis.core.DataValidateException;
import cn.cerc.mis.core.IService;
import cn.cerc.mis.core.ServiceState;

public class StartServices extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(StartServices.class);
    private static final long serialVersionUID = 1L;
    private static final PermissionPolice police = new PermissionPolice();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        log.debug(uri);
        req.setCharacterEncoding("UTF-8");

        String token = req.getParameter(ISession.TOKEN);
        DataSet dataIn = new DataSet(req.getParameter("dataIn"));
        String service = req.getPathInfo().substring(1);
        resp.setContentType("text/html;charset=UTF-8");
        DataSet dataOut = new DataSet();
        String sid = req.getSession().getId();
        if (service == null) {
            dataOut.setMessage("service is null.");
            resp.getWriter().write(dataOut.toString());
            return;
        }
        // 执行指定函数
        try (BasicHandle handle = new BasicHandle()) {
            handle.getSession().setProperty(ISession.REQUEST, req);
            handle.getSession().setProperty(Application.SessionId, sid);
            handle.getSession().loadToken(token);
            IService bean = null;
            try {
                bean = Application.getService(handle, service);
                if (bean == null) {
                    dataOut.setMessage(String.format("service(%s) is null.", service))
                            .setState(ServiceState.NOT_FIND_SERVICE);
                    resp.getWriter().write(dataOut.toString());
                    return;
                }
            } catch (ClassNotFoundException e) {
                dataOut.setMessage(e.getMessage()).setState(ServiceState.NOT_FIND_SERVICE);
                resp.getWriter().write(dataOut.toString());
                return;
            }
            String permission = police.getPermission(bean.getClass());
            if (!police.allowGuestUser(permission)) {
                ISession sess = handle.getSession();
                if ((sess == null) || (!sess.logon())) {
                    dataOut.setMessage("请您先登入系统").setState(ServiceState.ACCESS_DISABLED);
                    resp.getWriter().write(dataOut.toString());
                    return;
                }
                if (!police.checkPassed(handle.getSession().getPermissions(), permission)) {
                    dataOut.setMessage("您的执行权限不足").setState(ServiceState.ACCESS_DISABLED);
                    resp.getWriter().write(dataOut.toString());
                    return;
                }
            }
            try {
                dataOut = bean.execute(handle, dataIn);
            } catch (DataValidateException e) {
                dataOut.setMessage(e.getMessage());
            }
            if (dataOut == null)
                dataOut = new DataSet().setMessage("service return empty");
            resp.getWriter().write(dataOut.toString());
        } catch (Exception e) {
            Throwable err = e.getCause() != null ? e.getCause() : e;
            log.error(err.getMessage(), err);
            dataOut.setState(ServiceState.ERROR).setMessage(err.getMessage());
            resp.getWriter().write(dataOut.toString());
        }
    }

}
