package cn.cerc.ui.form;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIInput;

public class UIStringField extends UIAbstractField {
    // 表单元素类型
    private String inputType = UIInput.TYPE_TEXT;
    // 输入提示文字
    private String placeholder;
    private UIDescriptionInput input = new UIDescriptionInput(null);

    public UIStringField(UIComponent owner, String code) {
        super(owner, code);
        input.setInputType(inputType);
        input.setName(code);
    }

    public UIStringField(UIComponent owner, String code, String name) {
        super(owner, code, name);
        input.setInputType(inputType);
        input.setName(code);
    }

    public UIStringField(UIComponent owner, String code, String name, int width) {
        super(owner, code, name, width);
        input.setInputType(inputType);
        input.setName(code);
    }

    @Override
    public void writeContent(HtmlWriter html) {
        input.setValue(this.current().getString(this.getCode()));
        input.setPlaceholder(placeholder);
        input.output(html);
//        html.print("<input type='%s' name='%s' id='%s' value='%s' autocomplete='off'", this.getInputType(), this.getCode(), this.getCode(),
//                this.current().getString(this.getCode()));
//        if (!Utils.isEmpty(this.placeholder))
//            html.print(" placeholder='%s'", this.placeholder);
//        html.print(">");
    }

    public String getInputType() {
        return inputType;
    }

    public UIStringField setInputType(String inputType) {
        this.inputType = inputType;
        return this;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public UIStringField setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public UIStringField setDescriptionIcon(String imageSrc) {
        this.input.setDescriptionIcon(imageSrc);
        return this;
    }

    public UIStringField setDescriptionText(String text) {
        this.input.setDescriptionText(text);
        return this;
    }

}
