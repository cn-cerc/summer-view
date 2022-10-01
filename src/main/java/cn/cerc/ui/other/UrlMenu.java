package cn.cerc.ui.other;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UrlMenu extends UIComponent {
    private String name;

    public UrlMenu(UIComponent owner) {
        this(owner, null, null);
    }

    public UrlMenu(UIComponent owner, String name, String url) {
        super(owner);
        this.setRootLabel("a");
        this.setName(name);
        this.setUrl(url);
    }

    public String getName() {
        return name;
    }

    public UrlMenu setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return (String) this.getCssProperty("href");
    }

    public UrlMenu setUrl(String url) {
        this.setCssProperty("href", url);
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        html.print(name);
        this.endOutput(html);
    }

}
