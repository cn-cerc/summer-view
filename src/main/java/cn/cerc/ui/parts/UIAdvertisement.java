package cn.cerc.ui.parts;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.page.StaticFile;

public class UIAdvertisement extends UIComponent {
    private static final ClassConfig config = new ClassConfig(UIAdvertisement.class, SummerUI.ID);
    private String icon;

    public UIAdvertisement(UIComponent owner) {
        super(owner);
        var impl = Application.getBean(ImageConfigImpl.class);
        if (impl != null)
            this.icon = impl.getClassProperty(UIAdvertisement.class, SummerUI.ID, "icon", "");
        else
            this.icon = config.getClassProperty("icon", "");
    }

    @Override
    public void output(HtmlWriter html) {
        html.println("<div class=\"ad\">");
        html.println("<div class=\"ban_javascript clear\">");
        html.println("<ul>");
        html.println("<li><img src=\"%s\"></li>", StaticFile.getSummerImage(this.icon));
        html.println("</ul>");
        html.println("</div>");
        html.println("</div>");
    }
}
