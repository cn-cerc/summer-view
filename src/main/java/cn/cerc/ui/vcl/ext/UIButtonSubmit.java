package cn.cerc.ui.vcl.ext;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIButton;

public class UIButtonSubmit extends UIButton {

    public UIButtonSubmit(UIComponent owner) {
        super(owner);
        this.setType("submit");
        this.setName("submit");
        this.setText("submit");
        this.setValue("submit");
    }

}
