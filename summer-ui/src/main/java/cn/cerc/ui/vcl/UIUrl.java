package cn.cerc.ui.vcl;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UrlRecord;

public class UIUrl extends UIBaseHtml {
    private String text;
    private String href;
    private UrlRecord url;

    public UIUrl() {
        this(null);
    }

    public UIUrl(UIComponent owner) {
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

    public UIUrl setText(String text) {
        this.text = text;
        return this;
    }

    public String getHref() {
        return url == null ? href : url.getUrl();
    }

    public UIUrl setHref(String href) {
        this.href = href;
        return this;
    }

    // 此函数特别允许与setHref重复，以方便记忆
    @Deprecated
    public UIUrl setUrl(String href) {
        return this.setHref(href);
    }

    // 此函数特别允许与setHref重复，以方便记忆
    @Deprecated
    public UIUrl setUrl(String href, Object... args) {
        this.href = String.format(href, args);
        return this;
    }

    public String getOnclick() {
        return (String) this.readProperty("onclick");
    }

    public UIUrl setOnclick(String onclick) {
        this.writeProperty("onclick", onclick);
        return this;
    }
    
    public String getTarget() {
        return (String) this.readProperty("target");
    }

    public UIUrl setTarget(String target) {
        this.writeProperty("target", target);
        return this;
    }
   
    public UIUrl setSite(String site) {
        if (url == null)
            url = new UrlRecord();
        url.setSite(site);
        return this;
    }

    public UIUrl putParam(String key, String value) {
        if (url == null)
            url = new UrlRecord();
        url.putParam(key, value);
        return this;
    }

    public static void main(String[] args) {
        UIUrl item = new UIUrl();
        item.setOnclick(String.format("return confirm(\"%s\");", "abc"));
        System.out.println(item);

    }
}
