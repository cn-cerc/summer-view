package cn.cerc.ui.vcl;

import cn.cerc.ui.core.UIComponent;

public class UILi extends UIComponent {
    private UIText text;

    public UILi(UIComponent owner) {
        super(owner);
        this.setRootLabel("li");
        this.text = new UIText(this);
    }

    public String getText() {
        return text.getText();
    }

    public UILi setText(String value) {
        this.text.setText(value);
        return this;
    }

}
