package cn.cerc.ui.fields;

import cn.cerc.ui.core.UIComponent;

public class TextAreaField extends AbstractField {
    public TextAreaField(UIComponent owner, String name, String field) {
        super(owner, name, field);
        this.setHtmlTag("textarea");
    }

    public TextAreaField(UIComponent owner, String name, String field, int width) {
        super(owner, name, field);
        this.setWidth(width);
        this.setHtmlTag("textarea");
    }

}
