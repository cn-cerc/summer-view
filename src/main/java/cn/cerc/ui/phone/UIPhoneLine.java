package cn.cerc.ui.phone;

import cn.cerc.ui.core.UIComponent;

public class UIPhoneLine extends UIComponent {

    public UIPhoneLine(UIComponent owner) {
        super(owner);
        this.setRootLabel("div");
    }

    public UIPhoneItem addColumn(String fieldCode) {
        return new UIPhoneItem(this).setFieldCode(fieldCode);
    }

}
