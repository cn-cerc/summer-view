package cn.cerc.ui.form;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIInput;

public class UIBooleanField extends UIAbstractField {
    public UIBooleanField(UIComponent owner, String code) {
        super(owner, code);
    }

    public UIBooleanField(UIComponent owner, String code, String name) {
        super(owner, code, name);
    }

    public UIBooleanField(UIComponent owner, String code, String name, int width) {
        super(owner, code, name, width);
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("<div class='formEdit switchEdit' role='col%s'>", this.getWidth());
        html.print("<div>");
        if (!Utils.isEmpty(this.getName())) {
            html.print("<label for='%s'>", this.getCode());
            html.print("%s</label>", this.getName());
        }
        html.print("<span class='switchSpan");
        if (this.current().getBoolean(this.getCode()))
            html.print(" switch_checked");
        html.print("' onclick='switchToogle(this)'></span>");
        html.print("<input type='%s' name='%s' id='%s' value='1'", UIInput.TYPE_CHECKBOX, this.getCode(),
                this.getCode());
        if (this.current().getBoolean(this.getCode()))
            html.print(" checked");
        html.print(" onchange='switchUpdate(this)' />");
        html.print("</div>");
        if (this.dialog != null) {
            html.println("<span class='dialogSpan' onclick='%s'>%s</span>", this.dialog.toString(),
                    this.dialog.getText());
        }
        html.print("</div>");
    }

}
