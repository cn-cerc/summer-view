package cn.cerc.ui.form;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIInput;

public class UIStringField extends UIAbstractField {
    // 表单元素类型
    private String inputType = UIInput.TYPE_TEXT;

    public UIStringField(UIComponent owner, String code) {
        super(owner, code);
    }

    public UIStringField(UIComponent owner, String code, String name) {
        super(owner, code, name);
    }

    public UIStringField(UIComponent owner, String code, String name, int width) {
        super(owner, code, name, width);
    }

    @Override
    public void writeContent(HtmlWriter html) {
        html.print("<input type='%s' name='%s' id='%s' value='%s'>", this.getInputType(), this.getCode(),
                this.getCode(), this.current().getString(this.getCode()));
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

}
