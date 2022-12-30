package cn.cerc.ui.form;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIInput;

public class UIDoublieField extends UIAbstractField {
    private UIInput firstInput = new UIInput(null);
    private UIInput lastInput = new UIInput(null);

    public UIDoublieField(UIComponent owner, String firstName, String lastName) {
        super(owner);
        this.firstInput.setName(firstName).setId(firstName);
        this.lastInput.setName(lastName).setId(lastName);
    }

    public UIDoublieField(UIComponent owner, String firstName, String lastName, int width) {
        this(owner, firstName, lastName);
        this.setWidth(width);
    }

    @Override
    public void writeContent(HtmlWriter html) {
        if (Utils.isEmpty(firstInput.getName()) || Utils.isEmpty(lastInput.getName())) {
            throw new RuntimeException("input attribute name can not be empty.");
        }
        getFirstInput().setValue(this.current().getString(this.getFirstInput().getName()));
        getLastInput().setValue(this.current().getString(this.getLastInput().getName()));

        html.print(firstInput.toString());
        html.print(lastInput.toString());
    }

    public UIInput getFirstInput() {
        return firstInput;
    }

    public UIInput getLastInput() {
        return lastInput;
    }

    public UIDoublieField setProportion(int prop1, int prop2) {
        this.firstInput.setCssProperty("style", String.format("flex: %s", prop1));
        this.lastInput.setCssProperty("style", String.format("flex: %s", prop2));
        return this;
    }

    @Override
    public void updateField() {
        if (Utils.isEmpty(firstInput.getName()) || Utils.isEmpty(lastInput.getName())) {
            throw new RuntimeException("input attribute name can not be empty.");
        }
        this.updateValue(firstInput.getName(), firstInput.getName());
        this.updateValue(lastInput.getName(), lastInput.getName());
    }
}
