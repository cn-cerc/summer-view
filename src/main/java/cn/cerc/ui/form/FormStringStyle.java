package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;
import cn.cerc.ui.vcl.UIInput;

public class FormStringStyle extends FormStyleDefine {
    // 表单元素类型
    private String inputType = UIInput.TYPE_TEXT;
    // 输入提示文字
    private String placeholder;
    private UIDescriptionInput input = new UIDescriptionInput(null);

    public FormStringStyle(String code, DataRow dataRow) {
        super(code, dataRow);
        input.setName(code).setInputType(inputType);
    }

    public FormStringStyle(String name, String code, DataRow dataRow) {
        super(name, code, dataRow);
        input.setName(code).setInputType(inputType);
    }

    public FormStringStyle(String name, String code, int width, DataRow dataRow) {
        super(name, code, width, dataRow);
        input.setName(code).setInputType(inputType);
    }

    @Override
    public void buildHtml() {
        input.setValue(dataRow.getString(code));
        input.setPlaceholder(placeholder);
        builder.append(input.toString());
    }

    public String getInputType() {
        return inputType;
    }

    public FormStringStyle setInputType(String inputType) {
        this.inputType = inputType;
        return this;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public FormStringStyle setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public FormStringStyle setDescriptionIcon(String imageSrc) {
        this.input.setDescriptionIcon(imageSrc);
        return this;
    }

    public FormStringStyle setDescriptionText(String text) {
        this.input.setDescriptionText(text);
        return this;
    }

    public FormStringStyle setReadonly(boolean readonly) {
        this.input.setReadonly(readonly);
        return this;
    }

    public FormStringStyle setClickDialog(String dialogFunc) {
        this.input.setDialog(code, dialogFunc);
        return this;
    }

    public FormStringStyle setClickDialog(String dialogFunc, String... params) {
        this.input.setDialog(code, dialogFunc, params);
        return this;
    }

}
