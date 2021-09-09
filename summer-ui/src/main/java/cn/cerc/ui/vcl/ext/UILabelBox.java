package cn.cerc.ui.vcl.ext;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UILabelBox extends UIComponent {
    private String text;
    private String role;
    private String forid;

    public UILabelBox() {
        this(null);
    }

    public UILabelBox(UIComponent owner) {
        super(owner);
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("<label");
        super.outputPropertys(html);
        if (role != null) {
            html.print(" role='%s'", this.role);
        }
        if (forid != null) {
            html.print(" for='%s'", this.forid);
        }
        html.print(">");
        html.print(text);
        html.println("</label>");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getForid() {
        return forid;
    }

    public void setForid(String forid) {
        this.forid = forid;
    }

}
