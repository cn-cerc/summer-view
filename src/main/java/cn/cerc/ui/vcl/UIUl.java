package cn.cerc.ui.vcl;

import cn.cerc.ui.core.UIComponent;

public class UIUl extends UIComponent {

    public UIUl(UIComponent owner) {
        super(owner);
        this.setRootLabel("ul");
    }

    @Override
    public UIComponent addChild(UIComponent child) {
        if (child instanceof UILi)
            super.addChild(child);
        else {
            UILi item = this.addItem();
            item.addChild(child);
            super.addChild(item);
        }
        return this;
    }

    public UILi addItem() {
        return new UILi(this);
    }

}
