package cn.cerc.ui.vcl.ext;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

/**
 * 单选框
 *
 * @author 张弓
 */
public class UICheckBox extends UIComponent {
    private String name;
    private String type;
    private String value;
    private boolean checked;
    private boolean disabled;

    public UICheckBox() {
        this(null);
    }

    public UICheckBox(UIComponent owner) {
        super(owner);
        type = "checkbox";
        checked = false;
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("<input");
        super.outputPropertys(html);
        if (this.name != null) {
            html.print(" name='%s'", this.getName());
        }
        if (type != null)
            html.print(" type=\"%s\"", type);
        if (value != null)
            html.print(" value='%s'", this.value);
        if (!Utils.isEmpty(this.getRole()))
            html.print(" role='%s'", this.getRole());
        if (checked)
            html.print(" checked='checked'");
        if (disabled)
            html.print(" disabled='disabled'");
        html.println("/>");
    }

    public String getName() {
        return name;
    }

    public UICheckBox setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public UICheckBox setType(String type) {
        this.type = type;
        return this;
    }

    public String getValue() {
        return value;
    }

    public UICheckBox setValue(String value) {
        this.value = value;
        return this;
    }

    public boolean isChecked() {
        return checked;
    }

    public UICheckBox setChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
