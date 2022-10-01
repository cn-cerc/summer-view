package cn.cerc.ui.vcl;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UrlRecord;

public class UIUrl extends UIA {
    private UrlRecord url;
    private String hintMsg;

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

    public UIUrl setSite(String site) {
        if (url == null)
            url = new UrlRecord();
        if (site != null)
            url.setSite(site.replace("'", "\""));
        return this;
    }

    public UIUrl setSite(String format, Object... args) {
        this.setSite(String.format(format, args));
        return this;
    }

    public UIUrl putParam(String key, String value) {
        if (url == null)
            url = new UrlRecord();
        url.putParam(key, value);
        return this;
    }

    @Override
    public String getHref() {
        return url != null ? url.getUrl() : super.getHref();
    }

    public UIUrl setTitle(String title) {
        this.setProperty("title", title);
        return this;
    }

    public String getTitle() {
        return (String) this.getProperty("title");
    }

    public String getHintMsg() {
        return hintMsg;
    }

    public void setHintMsg(String hintMsg) {
        this.hintMsg = hintMsg;
        this.setOnclick(String.format("return confirm(\"%s\");", hintMsg));
    }

    @Deprecated
    public String getUrl() {
        return this.getHref();
    }

    public static String html(String url, String title) {
        return new UIUrl().setHref(url).setText(title).toString();
    }

}
