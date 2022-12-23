package cn.cerc.ui.form;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIDoublieField extends UIAbstractField {
    private UIDescriptionInput firstInput = new UIDescriptionInput(null);
    private UIDescriptionInput lastInput = new UIDescriptionInput(null);

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
        firstInput.setValue(this.current().getString(this.firstInput.getName()));
        lastInput.setValue(this.current().getString(this.lastInput.getName()));

        html.print(firstInput.toString());
        html.print(lastInput.toString());
    }
}
