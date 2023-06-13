package cn.cerc.ui.fields;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIFont;
import cn.cerc.ui.vcl.UIText;

public class UIStarFlag extends UIComponent {

    public UIStarFlag(UIComponent owner) {
        super(owner);
        this.setRootLabel("font").setCssProperty("role", "require");
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        html.print("*");
        this.endOutput(html);
    }

    public static void main(String[] args) {
        var font = new UIFont(null).addComponent(new UIText().setText("*"));
        System.out.println(font.toString());
        System.out.println(new UIStarFlag(null).toString());
    }

}
