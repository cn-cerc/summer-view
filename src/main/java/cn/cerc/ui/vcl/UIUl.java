package cn.cerc.ui.vcl;

import cn.cerc.ui.core.UIComponent;

public class UIUl extends UIComponent {

    public UIUl(UIComponent owner) {
        super(owner);
        this.setRootLabel("ul");
    }

    @Override
    public UIComponent addComponent(UIComponent child) {
        if (child instanceof UILi)
            super.addComponent(child);
        else {
            UILi item = this.addItem();
            item.addComponent(child);
            super.addComponent(item);
        }
        return this;
    }

    public UILi addItem() {
        return new UILi(this);
    }

}
