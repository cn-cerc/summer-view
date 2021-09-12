package cn.cerc.ui.fields;

import cn.cerc.ui.core.UIComponent;

public class DateTimeField extends AbstractField {

    public DateTimeField(UIComponent owner, String name, String field) {
        super(owner, name, field, 10);
        this.setAlign("center");
    }

    public DateTimeField(UIComponent owner, String name, String field, int width) {
        super(owner, name, field, width);
        this.setAlign("center");
    }

}
