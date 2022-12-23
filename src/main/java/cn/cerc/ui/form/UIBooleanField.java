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
        html.print("<span class='switchSpan' onclick='switchToogle(this)'></span>");
        html.print("<input type='%s' name='%s' id='%s' value='1' checked='%s'/>", UIInput.TYPE_CHECKBOX, this.getCode(),
                this.getCode(), this.current().getBoolean(this.getCode()));
    }

}
