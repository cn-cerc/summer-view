package cn.cerc.ui.vcl.ext;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIGroupBox extends UIComponent {

    public UIGroupBox(UIComponent content) {
        super(content);
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("<div role='group'");
        super.outputPropertys(html);
        html.println(">");
        super.output(html);
        html.println("</div>");
    }

}
