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
        this(owner);
        this.title = title;
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        super.beginOutput(html);
        if (this.title != null)
            html.println("<legend>%s</legend>", this.title);
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void endOutput(HtmlWriter html) {
        super.endOutput(html);
    }

}
