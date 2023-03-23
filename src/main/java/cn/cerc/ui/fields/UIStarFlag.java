package cn.cerc.ui.fields;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIFont;
import cn.cerc.ui.vcl.UIText;

public class UIStarFlag extends UIComponent {
    private UIText text;

    public UIStarFlag(UIComponent owner) {
        super(owner);
        this.setRootLabel("font");
        text = new UIText(this);
        text.setText("*");
    }

    public static void main(String[] args) {
        var font = new UIFont(null).addComponent(new UIText().setText("*"));
        System.out.println(font.toString());
        System.out.println(new UIStarFlag(null).toString());
    }

}