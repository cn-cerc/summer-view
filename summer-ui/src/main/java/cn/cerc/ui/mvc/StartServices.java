package cn.cerc.ui.mvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

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

        // 读取post的form表单参数
        Map<String, String[]> params = request.getParameterMap();
        Map<String, String> items = new HashMap<>();
        for (String key : params.keySet()) {
            String[] values = params.get(key);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            items.put(key, valueStr);
        }
        log.debug("request {}", new Gson().toJson(items));

        String token = items.get(ISession.TOKEN);
        log.debug("token {}", token);
        String text = items.get("dataIn");
        log.debug("dataIn {}", text);

        DataSet dataIn = new DataSet(text);
        String service = request.getPathInfo().substring(1);
        response.setContentType("text/html;charset=UTF-8");
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
