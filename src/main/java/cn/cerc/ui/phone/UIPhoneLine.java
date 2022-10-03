package cn.cerc.ui.phone;

import cn.cerc.ui.core.UIComponent;

public class UIPhoneLine extends UIBlockLine {

    public UIPhoneLine(UIComponent owner) {
        super(owner);
    }

    @Override
    public UIPhoneLine addCell(String... fieldList) {
        super.addCell(fieldList);
        return this;
    }

    @Override
    public UIPhoneCell newCell(String fieldCode) {
        return new UIPhoneCell(this).setFieldCode(fieldCode);
    }

}
