package cn.cerc.ui.vcl;

import cn.cerc.ui.core.UIComponent;

public class UITable extends UIBaseHtml {

    public UITable() {
        this(null);
    }

    public UITable(UIComponent component) {
        super(component);
        this.setRootLabel("table");
    }

}
