package cn.cerc.ui.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.core.DataSet;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.BasicHandle;
import cn.cerc.mis.core.IService;
import cn.cerc.mis.core.RequestData;
import cn.cerc.mis.core.ServiceState;

public class StartServices extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(StartServices.class);
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String uri = req.getRequestURI();
		log.debug(uri);
		req.setCharacterEncoding("UTF-8");
		RequestData param = new RequestData(req);

		String token = param.getToken();
		log.debug("DataIn Param==={}", param.getParam());
		resp.setContentType("text/html;charset=UTF-8");

		DataSet dataOut = new DataSet();
		String sid = req.getSession().getId();
		if (param.getService() == null) {
			dataOut.setMessage("class is null.");
			resp.getWriter().write(dataOut.toString());
			return;
		}
		// 执行指定函数
		log.debug(param.getService());
		try (BasicHandle handle = new BasicHandle()) {
			handle.getSession().setProperty(Application.SessionId, sid);
			handle.getSession().loadToken(token);
			IService bean = Application.getService(handle, param.getService());
			if (bean == null) {
				dataOut.setMessage(String.format("service(%s) is null.", param.getService()));
				resp.getWriter().write(dataOut.toString());
				return;
			}
			if (!bean.allowGuestUser(handle)) {
				dataOut.setState(ServiceState.ACCESS_DISABLED);
				dataOut.setMessage("请您先登入系统");
				resp.getWriter().write(dataOut.toString());
				return;
			}
			DataSet dataIn = new DataSet(param.getParam());
			dataOut = bean.execute(dataIn);
			resp.getWriter().write(dataOut.toString());
		} catch (Exception e) {
			Throwable err = e.getCause() != null ? e.getCause() : e;
			log.error(err.getMessage(), err);
			dataOut.setState(ServiceState.ERROR).setMessage(err.getMessage());
			resp.getWriter().write(dataOut.toString());
		}
	}
}
