package cn.cerc.ui.phone;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UISpan;

/**
 * 首页消息提示
 */
public class Block128 extends UICustomPhone {
    private UISpan title = new UISpan();

    public Block128(UIComponent owner) {
        super(owner);
        title.setText("(title)");
    }

    @Override
    public void output(HtmlWriter html) {
        html.println("<!-- %s -->", this.getClass().getName());
        html.print("<div class='block128'>");
        title.output(html);
        html.println("</div>");
    }

    public UISpan getTitle() {
        return title;
    }
}
