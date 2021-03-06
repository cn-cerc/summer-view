package cn.cerc.ui.phone;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UISpan;

/**
 * @author 张弓
 */
public class Block105 extends UICustomPhone {
    private UISpan title = new UISpan();

    /**
     * 普通的分段标题
     *
     * @param owner 内容显示区
     */
    public Block105(UIComponent owner) {
        super(owner);
        title.setText("(title)");
    }

    @Override
    public void output(HtmlWriter html) {
        html.println("<!-- %s -->", this.getClass().getName());
        html.print("<div class='block105'>");
        title.output(html);
        html.println("</div>");
    }

    public UISpan getTitle() {
        return title;
    }
}
