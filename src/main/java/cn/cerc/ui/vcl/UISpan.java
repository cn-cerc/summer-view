package cn.cerc.ui.vcl;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UISpan extends UIComponent implements IHtml {
    private UIText text;
    private UIComponent url;

    public UISpan() {
        this(null);
    }

    public UISpan(UIComponent owner) {
        super(owner);
        this.setRootLabel("span");
        this.url = new UIComponent(this);
        this.text = new UIText(url);
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        if (this.getUrl() != null) {
            url.setRootLabel("a");
            if (this.getTarget() == null)
                this.setTarget("_blank");
        }
        super.beginOutput(html);
    }

    public String getText() {
        return text.getText();
    }

    public UISpan setText(String text) {
        this.text.setText(text);
        return this;
    }

    public UISpan setText(String format, Object... args) {
        this.text.setText(String.format(format, args));
        return this;
    }

    public String getRole() {
        return (String) this.getCssProperty("role");
    }

    public UISpan setRole(String role) {
        this.setCssProperty("role", role);
        return this;
    }

    public String getOnclick() {
        return (String) this.getCssProperty("onclick");
    }

    public UISpan setOnclick(String onclick) {
        this.setCssProperty("onclick", onclick);
        return this;
    }

    @Deprecated
    private String getUrl() {
        return (String) this.url.getCssProperty("href");
    }

    public UISpan setUrl(String href) {
        this.url.setCssProperty("href", href);
        return this;
    }

    @Deprecated
    private String getTarget() {
        return (String) url.getCssProperty("target");
    }

    @Deprecated
    public UISpan setTarget(String target) {
        url.setCssProperty("target", target);
        return this;
    }

}
