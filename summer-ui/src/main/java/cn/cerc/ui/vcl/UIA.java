package cn.cerc.ui.vcl;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UrlRecord;

public class UIA extends UIComponent implements IHtml {
    private String text;
    private String href;
    private UrlRecord url;

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
        this.writeProperty("href", this.getHref());
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
        return url == null ? href : url.getUrl();
    }

    public UIA setHref(String href) {
        this.href = href;
        return this;
    }

    public String getOnclick() {
        return (String) this.readProperty("onclick");
    }

    public UIA setOnclick(String onclick) {
        this.writeProperty("onclick", onclick);
        return this;
    }
    
    public String getTarget() {
        return (String) this.readProperty("target");
    }

    public UIA setTarget(String target) {
        this.writeProperty("target", target);
        return this;
    }
   
    public UIA setSite(String site) {
        if (url == null)
            url = new UrlRecord();
        url.setSite(site);
        return this;
    }

    public UIA putParam(String key, String value) {
        if (url == null)
            url = new UrlRecord();
        url.putParam(key, value);
        return this;
    }

}
