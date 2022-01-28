package cn.cerc.ui.vcl;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIVideo extends UIComponent implements IHtml {

    private String src;
    private String width = "190";
    private String height = "90";

    public UIVideo(UIComponent owner) {
        super(owner);
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("<video ");
        if (this.getCssClass() != null)
            html.print(" class=\"%s\"", this.getCssClass());
        if (this.src != null)
            html.print(" src=\"%s\"", this.src);
        html.print(" width=\"%s\"", this.width);
        html.print(" height=\"%s\"", this.height);
        html.print("</>");
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

}
