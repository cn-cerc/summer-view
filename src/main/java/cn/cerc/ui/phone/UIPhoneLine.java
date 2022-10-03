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
    public void createCell(String fieldCode) {
        new UIPhoneCell(this).setFieldCode(fieldCode);
    }

    public UIPhoneCell getCell(int index) {
        return (UIPhoneCell) this.getComponent(index);
    }

}
