package cn.cerc.ui.vcl;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIP extends UIComponent {
    private String text;

    public UIP(UIComponent owner) {
        super(owner);
        this.setRootLabel("p");
    }

    @Override
    public void endOutput(HtmlWriter html) {
        if (this.text != null)
            html.print(text);
        super.endOutput(html);
    }

    public final String getText() {
        return text;
    }

    public final UIP setText(String text) {
        this.text = text;
        return this;
    }

}
