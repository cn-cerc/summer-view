package cn.cerc.ui.ssr.page;

import cn.cerc.db.core.ISession;
import cn.cerc.mis.core.AbstractForm;
import cn.cerc.mis.core.IPage;

public class StubForm extends AbstractForm {

    public StubForm(ISession session) {
        super();
        this.setSession(session);
    }

    @Override
    public IPage execute() throws Exception {
        return null;
    }

    @Override
    public String _call(String funcId) throws Exception {
        return super.callDefault(funcId);
    }

}
