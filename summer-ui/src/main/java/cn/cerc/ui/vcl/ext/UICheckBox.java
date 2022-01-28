package cn.cerc.ui.vcl.ext;

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
    private String role;
    private boolean checked;

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
        if (type != null) {
            html.print(" type=\"%s\"", type);
        }
        if (value != null) {
            html.print(" value='%s'", this.value);
        }
        if (role != null) {
            html.print(" role='%s'", this.role);
        }
        if (checked) {
            html.print(" checked='checked'");
        }
        html.println("/>");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
