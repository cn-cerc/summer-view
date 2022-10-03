package cn.cerc.ui.phone;

import cn.cerc.ui.core.UIComponent;

public abstract class UIBlockLine extends UIComponent {

    public UIBlockLine(UIComponent owner) {
        super(owner);
        this.setRootLabel("div");
    }

    @Override
    public UIBlockLine addComponent(UIComponent child) {
        super.addComponent(child);
        return this;
    }

    public UIPhoneCell getCell(int index) {
        return (UIPhoneCell) this.getComponent(index);
    }

    abstract UIBlockLine addCell(String... fields);

}
