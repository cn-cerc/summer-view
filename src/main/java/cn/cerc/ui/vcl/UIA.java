package cn.cerc.ui.vcl;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIA extends UIComponent implements IHtml {
    private String text;
    private String href;

    @Deprecated
    public UIA() {
        this(null);
    }

    public UIA(UIComponent owner) {
        super(owner);
        this.setRootLabel("a");
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        this.setProperty("href", this.getHref());
        super.beginOutput(html);
    }

    @Override
    public void endOutput(HtmlWriter html) {
        if (this.text != null)
            html.print(this.text);
        super.endOutput(html);
    }

    public String getText() {
        return text;
    }

    public UIA setText(String text) {
        this.text = text;
        return this;
    }

    public String getHref() {
        return this.href;
    }

    public UIA setHref(String href) {
        this.href = href;
        return this;
    }

    public String getOnclick() {
        return (String) this.getProperty("onclick");
    }

    public UIA setOnclick(String onclick) {
        this.setProperty("onclick", onclick);
        return this;
    }

    public String getTarget() {
        return (String) this.getProperty("target");
    }

    public UIA setTarget(String target) {
        this.setProperty("target", target);
        return this;
    }

}
