package cn.cerc.ui.phone;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIImage;
import cn.cerc.ui.vcl.UISpan;
import cn.cerc.ui.vcl.ext.UITextBox;

public class Block109 extends UICustomPhone {
    private UISpan label = new UISpan();
    private UITextBox input = new UITextBox();
    private UIImage select = new UIImage();

    /**
     * 文本 + 输入框 + 弹窗选择按钮
     *
     * @param owner 内容显示区
     */
    public Block109(UIComponent owner) {
        super(owner);
        label.setText("(label)");
        select.setSrc(getImage("jui/phone/block109-select.png"));
    }

    @Override
    public void output(HtmlWriter html) {
        html.println("<!-- %s -->", this.getClass().getName());
        html.print("<div class='block109'>");
        label.output(html);
        input.output(html);
        select.output(html);
        html.println("</div>");
    }

    public UISpan getLabel() {
        return label;
    }

    public UITextBox getInput() {
        return input;
    }

    public UIImage getSelect() {
        return select;
    }
}
