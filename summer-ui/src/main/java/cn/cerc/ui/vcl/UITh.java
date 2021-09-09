package cn.cerc.ui.vcl;

import cn.cerc.ui.core.UIComponent;

public class UITh extends UIBaseHtml {
    private UIText text;

    public UITh(UIComponent component) {
        super(component);
        this.setRootLabel("th");
        this.text = new UIText(this);
    }

    public UITh() {
        this(null);
    }

    public String getText() {
        return text.getText();
    }

    public UITh setText(String text) {
        this.text.setText(text);
        return this;
    }

}
