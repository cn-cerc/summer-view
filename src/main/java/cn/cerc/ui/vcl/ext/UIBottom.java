package cn.cerc.ui.vcl.ext;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIBottom extends UIComponent {
    private String caption;
    private String url;
    private String target;

    public UIBottom(UIComponent owner) {
        super(owner);
    }

    public String getName() {
        return caption;
    }

    public UIBottom setCaption(String caption) {
        this.caption = caption;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public UIBottom setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public UIBottom setTarget(String target) {
        this.target = target;
        return this;
    }

    @Override
    public UIBottom setCssClass(String cssClass) {
        super.setCssClass("bottomBotton " + cssClass);
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("<div");
        super.outputPropertys(html);
        html.print(">");

        html.print("<a href=\"%s\"", this.url);
        if (!Utils.isEmpty(this.getTarget())) {
            html.print(" target=\"%s\"", this.target);
        }
        html.println(">%s</a>", this.caption);

        html.println("</div>");
    }

}
