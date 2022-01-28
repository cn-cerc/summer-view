package cn.cerc.ui.vcl;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIHr extends UIComponent implements IHtml {

    public UIHr(UIComponent owner) {
        super(owner);
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("<hr/>");
    }

}
