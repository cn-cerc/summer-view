package cn.cerc.ui.grid;

import cn.cerc.db.core.DataCell;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.UISelectDialog;
import cn.cerc.ui.vcl.UIInput;

public class UIStringDataStyle extends UIAbstractDataStye {

    private String inputType;

    public UIStringDataStyle(UIDataStyle styleData, DataCell data, boolean inGrid) {
        super(styleData, data, inGrid);
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
            if (inputType != null)
                input.setInputType(inputType);
            // 允许外部更改input组件的属性
            define.output(input);
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

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

}
