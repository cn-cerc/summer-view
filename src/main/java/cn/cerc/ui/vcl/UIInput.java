package cn.cerc.ui.vcl;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.INameOwner;
import cn.cerc.ui.core.UIComponent;

public class UIInput extends UIComponent implements IHtml, INameOwner {

    public static final String TYPE_BUTTON = "button";
    public static final String TYPE_CHECKBOX = "checkbox";
    public static final String TYPE_RADIO = "radio";
    public static final String TYPE_PASSWORD = "password";
    public static final String TYPE_SUBMIT = "submit";
    public static final String TYPE_FILE = "file";
    public static final String TYPE_DATE = "date";
    public static final String TYPE_DATETIME_LOCAL = "datetime-local";
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_NUMBER = "number";

    private String title;
    private String name;
    private String value;
    private boolean hidden;
    private String inputType = TYPE_TEXT;

    public UIInput(UIComponent owner) {
        super(owner);
        this.setRootLabel("input");
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        if (this.title != null)
            html.print(this.title);
        this.setCssProperty("autocomplete", "off");// 默认关闭浏览器表单自动记录
        this.setCssProperty("name", this.getName());
        this.setCssProperty("type", this.hidden ? "hidden" : this.inputType);
        this.setCssProperty("value", this.value);
        super.beginOutput(html);
    }

    public String getCaption() {
        return title;
    }

//    @Deprecated
//    public UIInput setCaption(String caption) {
//        this.title = caption;
//        return this;
//    }

    @Override
    public String getName() {
        return name != null ? this.name : this.getId();
    }

    public UIInput setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public UIInput setValue(String value) {
        this.value = value;
        return this;
    }

    public boolean isReadonly() {
        return this.getSignProperty("readonly");
    }

    public void setReadonly(boolean readonly) {
        this.setSignProperty("readonly", readonly);
    }

    public String getPlaceholder() {
        return (String) this.getCssProperty("placeholder");
    }

    public void setPlaceholder(String placeholder) {
        this.setCssProperty("placeholder", placeholder);
    }

    public String getInputType() {
        return inputType;
    }

    public UIInput setInputType(String inputType) {
        this.inputType = inputType;
        return this;
    }

    public boolean isRequired() {
        return this.getSignProperty("required");
    }

    public void setRequired(boolean required) {
        this.setSignProperty("required", required);
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isChecked() {
        return this.getSignProperty("checked");
    }

    public void setChecked(boolean checked) {
        this.setSignProperty("checked", checked);
    }

    public static String html(String key, String value) {
        UIInput input = new UIInput(null);
        input.setId(key);
        input.setValue(value);
        return input.toString();
    }

}
