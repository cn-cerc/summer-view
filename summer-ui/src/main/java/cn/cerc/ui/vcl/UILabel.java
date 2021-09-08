package cn.cerc.ui.vcl;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UILabel extends UIBaseHtml {
    private String text;
    private String url;
    private String focusTarget;

    public UILabel(UIComponent component) {
        super(component);
    }

    public UILabel() {
        super(null);
    }

    public UILabel(String text) {
        super(null);
        this.text = text;
    }

    public UILabel(String text, String url) {
        super(null);
        this.text = text;
        this.url = url;
    }

    public String getFocusTarget() {
        return focusTarget;
    }

    public void setFocusTarget(String focusTarget) {
        this.focusTarget = focusTarget;
    }

    @Override
    public void output(HtmlWriter html) {
        if (url == null) {
            html.print("<label");
            if (focusTarget != null) {
                html.print(" for='%s'", focusTarget);
            }
            super.appendPropertys(html);
            if (this.url != null)
                html.print(" href='%s'");
            html.print(">");
            for (UIComponent item : this)
                item.output(html);
            if (this.text != null)
                html.print(this.text);
            html.print("</label>");
        } else {
            html.print("<a");
            super.appendPropertys(html);
            if (this.url != null)
                html.print(" href='%s'", this.url);
            html.print(">");
            for (UIComponent item : this)
                item.output(html);
            if (this.text != null)
                html.print(this.text);
            html.print("</a>");
        }
    }

    /**
     * 请改使用 getText
     * 
     * @return 返回 text 值
     */
    @Deprecated
    public String getCaption() {
        return text;
    }

    /**
     * 请改使用 setText
     * 
     * @param caption 设置 text 的值
     */
    @Deprecated
    public void setCaption(String caption) {
        this.text = caption;
    }

    public String getText() {
        return text;
    }

    public UILabel setText(String text) {
        this.text = text;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public UILabel setUrl(String url) {
        this.url = url;
        return this;
    }

}
