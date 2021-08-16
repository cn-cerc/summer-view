package cn.cerc.ui.mvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
import cn.cerc.mis.core.ServiceException;
import cn.cerc.mis.core.ServiceState;

public class StartServices extends HttpServlet {

    private static final long serialVersionUID = 2699818753661287159L;
    private static final Logger log = LoggerFactory.getLogger(StartServices.class);
    private static final PermissionPolice police = new PermissionPolice();

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
        String token = request.getParameter(ISession.TOKEN);
        String text = request.getParameter("dataIn");
        DataSet dataIn = new DataSet().fromJson(text);
        String service = request.getPathInfo().substring(1);
        response.setContentType("text/html;charset=UTF-8");

        log.debug("token {}", token);
        log.debug("dataIn {}", text);

        DataSet dataOut = new DataSet();
        if (service == null) {
            dataOut.setMessage("service is null.");
            response.getWriter().write(dataOut.toString());
            return;
        }

        // 执行指定函数
        try (BasicHandle handle = new BasicHandle()) {
            handle.getSession().setProperty(ISession.REQUEST, request);
            handle.getSession().setProperty(Application.SessionId, request.getSession().getId());
            handle.getSession().loadToken(token);
            IService bean = Application.getService(handle, service);
            if (bean == null) {
                dataOut.setMessage(String.format("service(%s) is null.", service))
                        .setState(ServiceState.NOT_FIND_SERVICE);
                response.getWriter().write(dataOut.toString());
                return;
            }
            String permission = police.getPermission(bean.getClass());
            if (!police.allowGuestUser(permission)) {
                ISession sess = handle.getSession();
                if ((sess == null) || (!sess.logon())) {
                    dataOut.setMessage("请您先登入系统").setState(ServiceState.ACCESS_DISABLED);
                    response.getWriter().write(dataOut.toString());
                    return;
                }
                if (!police.checkPassed(handle.getSession().getPermissions(), permission)) {
                    dataOut.setMessage("您的执行权限不足").setState(ServiceState.ACCESS_DISABLED);
                    response.getWriter().write(dataOut.toString());
                    return;
                }
            }
            dataOut = bean.execute(handle, dataIn);
            if (dataOut == null) {
                dataOut = new DataSet().setMessage("service return empty");
            }
            response.getWriter().write(dataOut.toString());
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
