package cn.cerc.ui.vcl;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIUl extends UIComponent {

    public UIUl(UIComponent owner) {
        super(owner);
        this.setRootLabel("ul");
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        for (UIComponent item : this) {
            html.print("<li>");
            item.output(html);
            html.print("</li>");
        }
        this.endOutput(html);
    }

}
