package cn.cerc.ui.vcl.ext;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIGroupBox extends UIComponent {
    private String title;

    public UIGroupBox(UIComponent owner) {
        super(owner);
        this.setRootLabel("div");
        this.writeProperty("role", "group");
    }

    public UIGroupBox(UIComponent owner, String title) {
        super(owner);
        this.writeProperty("role", "group");
        this.title = title;
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        super.beginOutput(html);
        if (this.title != null)
            html.println("<fieldset><legend>%s</legend>", this.title);
    }

    @Override
    public void endOutput(HtmlWriter html) {
        if (this.title != null)
            html.println("</fieldset>");
        super.endOutput(html);
    }

    public String getTitle() {
        return title;
    }

}
