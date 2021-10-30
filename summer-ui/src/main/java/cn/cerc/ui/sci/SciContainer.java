package cn.cerc.ui.sci;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class SciContainer extends UIComponent {

    public SciContainer(UIComponent owner) {
        super(owner);
        this.setClientRender(true);
    }

    @Override
    public void output(HtmlWriter html) {
        // 客户端渲染时，宿主
        html.println("<div id='%s'></div>", this.getId());
        html.println("<script>");
        html.println("let app = new aui.TPage()");
        html.println("app.container = '%s'", this.getId());
        for (UIComponent component : this)
            component.output(html);
        html.println("app.run()");
        html.println("</script>");
    }
}
