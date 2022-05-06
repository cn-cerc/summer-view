package cn.cerc.ui.phone;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIButton;
import cn.cerc.ui.vcl.UISpan;
import cn.cerc.ui.vcl.UITextarea;

public class Block111 extends UICustomPhone {
    private UISpan label = new UISpan();
    private UITextarea input = new UITextarea();
    private UIButton search = new UIButton();

    /**
     * 文本 + 输入框 + 查询按钮
     *
     * @param owner 内容显示区
     */
    public Block111(UIComponent owner) {
        super(owner);
        label.setText("(label)");
        search.setText("查询");
    }

    @Override
    public void output(HtmlWriter html) {
        html.println("<!-- %s -->", this.getClass().getName());
        html.print("<div class='block111'>");
        label.output(html);
        input.output(html);
        search.output(html);
        html.println("</div>");
    }

    public UISpan getLabel() {
        return label;
    }

    public UITextarea getInput() {
        return input;
    }

    public UIButton getSearch() {
        return search;
    }
}
