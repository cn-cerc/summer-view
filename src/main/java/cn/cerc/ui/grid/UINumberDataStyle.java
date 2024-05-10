package cn.cerc.ui.grid;

import cn.cerc.db.core.DataCell;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.UISelectDialog;
import cn.cerc.ui.vcl.UIInput;

public class UINumberDataStyle extends UIAbstractDataStyle {
    private String step = "any";

    public UINumberDataStyle(UIDataStyle owner, DataCell data, boolean inGrid) {
        super(owner, data, inGrid);
    }

    @Override
    public String getText(String defaultText) {
        if (define != null && !define.readonly()) {
            UIComponent box = new UIComponent(null);
            //
            UIInput input = new UIInput(box);
            input.setId(data.key());
            input.setValue(defaultText);
            if (define.width() > 0) {
                String width = String.format("width: %dpx", define.width() * UIDataStyle.PX_SIZE);
                input.setCssStyle(width);
            } else
                input.setCssStyle(null);
            input.setPlaceholder(define.placeholder());
            input.setInputType(UIInput.TYPE_NUMBER);
            input.setCssProperty("step", step);
            //
            if (!Utils.isEmpty(define.dialog()))
                new UISelectDialog(box).setDialog(define.dialog()).setInputId(data.key());
            //
            return box.toString();
        } else if (!owner.readonly()) {
            return getDisplayText(defaultText);
        } else
            return defaultText;
    }

    public String getStep() {
        return step;
    }

    public UINumberDataStyle setStep(String step) {
        this.step = step;
        return this;
    }

}
