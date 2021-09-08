package cn.cerc.ui.vcl;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIUl extends UIComponent {

    public UIUl() {
        super();
    }

    public UIUl(UIComponent owner) {
        super(owner);
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("<ul");
        super.appendPropertys(html);
        html.print(">");
        for (UIComponent item : this) {
            html.print("<li>");
            item.output(html);
            html.print("</li>");
        }
        html.println("</ul>");
    }

}
