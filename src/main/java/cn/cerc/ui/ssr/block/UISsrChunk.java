package cn.cerc.ui.ssr.block;

import cn.cerc.ui.core.UIComponent;

@Deprecated
public class UISsrChunk extends VuiChunk {

    public UISsrChunk() {
        super(null);
        template = new SsrTemplate();
    }

    public UISsrChunk(UIComponent owner) {
        super(owner);
    }

    public UISsrChunk(UIComponent owner, String templateText) {
        super(owner);
    }

    public UISsrChunk(UIComponent owner, Class<?> class1, String id) {
        super(owner);
    }

}
