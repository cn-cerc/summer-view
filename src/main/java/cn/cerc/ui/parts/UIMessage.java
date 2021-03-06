package cn.cerc.ui.parts;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIMessage extends UIComponent {
    private String text = "";

    public UIMessage(UIComponent owner) {
        super(owner);
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("<section role='message'");
        super.outputPropertys(html);
        html.print(">");
        if (!"".equals(text)) {
            html.print(text);
        }
        html.println("</section>");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
