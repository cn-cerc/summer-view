package cn.cerc.ui.phone;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIButton;
import cn.cerc.ui.vcl.UIImage;
import cn.cerc.ui.vcl.UISpan;

/**
 * @author 张弓
 */
public class Block991 extends UICustomPhone {
    private UIImage image = new UIImage();
    private UIButton button = new UIButton(this);
    private UISpan remark = new UISpan(this);

    /**
     * 底部状态栏：1个功能按钮+提示文字
     *
     * @param owner 内容显示区
     */
    public Block991(UIComponent owner) {
        super(owner);
        image.setSrc(getImage("jui/phone/block991_back.png"));
        button.setText("(button)");
        remark.setText("(remark)");
    }

    @Override
    public void output(HtmlWriter html) {
        html.println("<!-- %s -->", this.getClass().getName());
        html.println("<div class=\"block991\">");
        image.output(html);
        button.output(html);
        remark.output(html);
        html.println("</div>");
    }

    public UISpan getRemark() {
        return remark;
    }

    public UIButton getButton() {
        return button;
    }

    public UIImage getImage() {
        return image;
    }
}
