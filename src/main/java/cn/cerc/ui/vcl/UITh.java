package cn.cerc.ui.vcl;

import cn.cerc.ui.core.UIComponent;

public class UITh extends UIComponent implements IHtml {
    private UIText text;
    
    @Deprecated
    public UITh() {
        this(null);
    }

    public UITh(UIComponent component) {
        super(component);
        this.setRootLabel("th");
        this.text = new UIText(this);
    }

    public String getText() {
        return text.getText();
    }

    public UITh setText(String text) {
        this.text.setText(text);
        return this;
    }

}
