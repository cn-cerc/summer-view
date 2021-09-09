package cn.cerc.ui.vcl;

import cn.cerc.ui.core.UIComponent;

public class UITr extends UIComponent {

    public UITr() {
        this(null);
    }

    public UITr(UIComponent component) {
        super(component);
        this.setRootLabel("tr");
    }

}
