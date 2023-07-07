package cn.cerc.ui.fields;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UITextarea;

public class TextAreaField extends AbstractField {
    private UITextarea input;

    public TextAreaField(UIComponent owner, String name, String field) {
        this(owner, name, field, 0);
    }

    public TextAreaField(UIComponent owner, String name, String field, int width) {
        super(owner, name, field);
        this.setWidth(width);
        this.input = new UITextarea(this);
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        input.setId(this.getId());
        input.setName(this.getId());
        input.setReadonly(this.readonly());
//        input.setCssStyle(this.isResize() ? "resize: none;" : null);
        input.setSignProperty("required", this.isRequired());
        input.setSignProperty("autofocus", this.isAutofocus());
        input.setCssProperty("placeholder", this.getPlaceholder());
        String value = this.getValue();
        input.setText(value != null ? value : this.getText());
        input.output(html);
        this.endOutput(html);
    }

    public TextAreaField setRows(int rows) {
        input.setCssProperty("rows", rows > 0 ? rows : null);
        return this;
    }

    public TextAreaField setCols(int cols) {
        input.setCssProperty("cols", cols > 0 ? cols : null);
        return this;
    }

    @Override
    public TextAreaField setPlaceholder(String placeholder) {
        super.setPlaceholder(placeholder);
        return this;
    }

    public TextAreaField setMaxlength(int maxlength) {
        input.setCssProperty("maxlength", maxlength > 0 ? maxlength : null);
        return this;
    }

}
