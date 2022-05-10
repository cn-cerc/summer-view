package cn.cerc.ui.vcl;

import cn.cerc.ui.core.UIComponent;

public class UIMark extends UIText {
    
    public UIMark(UIComponent owner) {
        super(owner);
        this.setRootLabel("mark");
    }

    public UIMark() {
        this(null);
    }

    @Override
    public UIMark setText(String text) {
        super.setText(text);
        return this;
    }
}
