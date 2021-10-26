package cn.cerc.ui.columns;

import cn.cerc.ui.core.UIComponent;

public class UIGridLine extends UIComponent {

    public UIGridLine(UIComponent owner) {
        super(owner);
    }

    @Override
    public UIComponent addComponent(UIComponent component) {
        if (!(component instanceof IColumn)) {
            throw new RuntimeException("component is not IColumn");
        }
        super.addComponent(component);
        return this;
    }
}
