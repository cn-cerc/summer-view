package cn.cerc.ui.sci;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIDiv;

public class SciContainer extends UIComponent {

    public SciContainer(UIComponent owner) {
        super(owner);
        this.setRootLabel("script");
        this.setClientRender(true);
    }

    @Override
    public void output(HtmlWriter html) {
        // 客户端渲染时，宿主
        new UIDiv().setId(getId()).output(html);
        html.print("\n<").print(getRootLabel()).println(" type='module'>");
        html.println("import * as sci from '/static/sci/SummerCI.js'");
        html.println("let app = new sci.TPage()");
        html.println("app.setContainer('%s')", this.getId());
        for(UIComponent component : this)
            component.output(html);
        html.println("app.run()");
        super.endOutput(html);
    }
}
