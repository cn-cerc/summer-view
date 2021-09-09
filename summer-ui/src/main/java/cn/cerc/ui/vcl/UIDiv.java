package cn.cerc.ui.vcl;

import cn.cerc.ui.core.UIComponent;

public class UIDiv extends UIBaseHtml {
    private UIText text;

    public UIDiv() {
        this(null);
    }

    public UIDiv(UIComponent owner) {
        super(owner);
        this.setRootLabel("div");
        this.text = new UIText(this);
    }

    public UIDiv setText(String text) {
        this.text.setContent(text);
        return this;
    }

    public UIDiv setText(String format, Object... args) {
        this.text.setContent(String.format(format, args));
        return this;
    }

    public String getText() {
        return text.getContent();
    }

}
