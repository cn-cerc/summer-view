package cn.cerc.ui.form;

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
    public void writeContent(HtmlWriter html) {
        html.println("<span class='switchSpan' onclick='switchToogle(this)'></span>");
        html.println("<input type='%s' name='%s' value='%s'/>", UIInput.TYPE_CHECKBOX, this.getCode(),
                this.getRecord().getString(this.getCode()));
    }

}
