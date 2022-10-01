package cn.cerc.ui.phone;

import cn.cerc.ui.core.UIComponent;

public class UIPhoneView extends UIBlockView {

    public UIPhoneView(UIComponent owner) {
        super(owner);
        this.setActive(this.isPhone());
    }

}
