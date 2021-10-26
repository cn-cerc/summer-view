package cn.cerc.ui.fields;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class ButtonField extends AbstractField {
    private String data;
    private String type;

    @Deprecated
    public ButtonField() {
        super(null, null, null);
    }

    public ButtonField(UIComponent owner, String name, String field) {
        super(owner, name, field);
    }

    public ButtonField(UIComponent owner, String name, String field, String data) {
        super(owner, name, field);
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public ButtonField setData(String data) {
        this.data = data;
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("<button name=\"%s\"", this.getId());
        if (this.data != null) {
            html.print(" value=\"%s\"", this.data);
        }
        if (getCSSClass_phone() != null) {
            html.print(" class=\"%s\"", getCSSClass_phone());
        }
        if (this.getOnclick() != null) {
            html.print(" onclick=\"%s\"", this.getOnclick());
        }
        if (this.type != null) {
            html.print(" type=\"%s\"", this.type);
        }
        html.print(">");
        html.print("%s</button>", this.getName());
    }

    public String getType() {
        return type;
    }

    public ButtonField setType(String type) {
        this.type = type;
        return this;
    }
}
