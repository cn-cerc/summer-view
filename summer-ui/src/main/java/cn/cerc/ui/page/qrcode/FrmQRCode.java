package cn.cerc.ui.page.qrcode;

import cn.cerc.mis.core.AbstractForm;
import cn.cerc.mis.core.AppClient;
import cn.cerc.mis.core.IPage;
import cn.cerc.ui.page.service.SvrAutoLogin;

public class FrmQRCode extends AbstractForm implements JayunEasyLogin {

    @Override
    public IPage execute() throws Exception {
        new JayunQrcode(this.getRequest(), this.getResponse()).execute(this);
        return null;
    }

    @Override
    public JayunMessage getLoginToken() {
        JayunMessage message;
        SvrAutoLogin svrLogin = new SvrAutoLogin(this);
        if (svrLogin.check(this, this.getRequest())) {
            AppClient info = ((AppClient) this.getClient());
            message = new JayunMessage(true, "已确认");
            message.setToken(info.getToken());
            message.setUrl("WebDefault");
        } else {
            message = new JayunMessage(false, svrLogin.getMessage());
        }
        return message;
    }

    @Override
    public String getNotifyUrl() {
        return "FrmSecurity";
    }

    @Override
    public boolean logon() {
        return true;
    }

}
