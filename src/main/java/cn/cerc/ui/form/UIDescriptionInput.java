package cn.cerc.ui.form;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIInput;

public class UIDescriptionInput extends UIComponent {
    private UIInput input = new UIInput(this);
    private UIComponent description;

    public UIDescriptionInput(UIComponent owner) {
        super(owner);
        this.setRootLabel("div");
        this.setCssClass("descriptionInput");
    }

    public UIInput getInput() {
        return input;
    }

    public void setDescription(UIComponent description) {
        this.description = description;
        this.description.setOwner(this);
    }

    public String getName() {
        return this.input.getName() != null ? this.input.getName() : this.input.getId();
    }

    public UIDescriptionInput setName(String name) {
        this.input.setName(name);
        return this;
    }

    public String getValue() {
        return this.input.getValue();
    }

    public UIDescriptionInput setValue(String value) {
        this.input.setValue(value);
        return this;
    }

    public boolean isReadonly() {
        return this.input.getSignProperty("readonly");
    }

    public void setReadonly(boolean readonly) {
        this.input.setSignProperty("readonly", readonly);
    }

    public String getPlaceholder() {
        return (String) this.input.getCssProperty("placeholder");
    }

    public void setPlaceholder(String placeholder) {
        this.input.setCssProperty("placeholder", placeholder);
    }

    public String getInputType() {
        return this.input.getInputType();
    }

    public void setInputType(String inputType) {
        this.input.setInputType(inputType);
    }

    public boolean isRequired() {
        return this.input.getSignProperty("required");
    }

    public UIDescriptionInput setRequired(boolean required) {
        this.input.setSignProperty("required", required);
        return this;
    }

    public boolean isHidden() {
        return this.input.isHidden();
    }

    public UIDescriptionInput setHidden(boolean hidden) {
        this.input.setHidden(hidden);
        return this;
    }

    public boolean isChecked() {
        return this.input.getSignProperty("checked");
    }

    public UIDescriptionInput setChecked(boolean checked) {
        this.input.setSignProperty("checked", checked);
        return this;
    }

}
