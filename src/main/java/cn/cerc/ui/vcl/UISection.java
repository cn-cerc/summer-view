package cn.cerc.ui.vcl;

import cn.cerc.ui.core.UIComponent;

public class UISection extends UIComponent implements IHtml {

    public UISection(UIComponent owner) {
        super(owner);
        this.setRootLabel("section");
    }

}
