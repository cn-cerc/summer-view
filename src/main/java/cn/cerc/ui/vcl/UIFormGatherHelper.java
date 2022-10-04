package cn.cerc.ui.vcl;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.IHandle;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIForm.UIFormGatherImpl;

public class UIFormGatherHelper {
    private static final Logger log = LoggerFactory.getLogger(UIFormGatherHelper.class);
    private UIComponent root;
    private HttpServletRequest request;
    private int total;

    public UIFormGatherHelper(UIComponent root, HttpServletRequest request) {
        super();
        this.root = root;
        var impl = root.findOwner(IHandle.class);
        if (impl != null && impl.getRequest() != null) {
            this.request = impl.getRequest();
            gatherReuqest(this.root);
        } else {
            log.error("request is null");
        }
    }

    private void gatherReuqest(UIComponent parent) {
        for (var item : parent)
            gatherReuqest(item);
        if (parent instanceof UIFormGatherImpl impl)
            total += impl.gatherRequest(request);
    }

    public int total() {
        return total;
    }

}
