package cn.cerc.ui.vcl;

import cn.cerc.ui.core.UIComponent;

public class UIUrl extends UIA {
    
    @Deprecated
    public UIUrl() {
        this(null);
    }

    public UIUrl(UIComponent owner) {
        super(owner);
    }

    @Deprecated
    public UIUrl setUrl(String href) {
        this.setHref(href);
        return this;
    }

    @Deprecated
    public UIUrl setUrl(String href, Object... args) {
        this.setHref(String.format(href, args));
        return this;
    }

    @Override
    public UIUrl setText(String text) {
        super.setText(text);
        return this;
    }
    
    @Override
    public UIUrl setHref(String text) {
        super.setHref(text);
        return this;
    }
    
}
