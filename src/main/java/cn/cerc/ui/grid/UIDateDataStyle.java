package cn.cerc.ui.grid;

import cn.cerc.db.core.DataCell;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.DateField;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.fields.UISelectDialog;
import cn.cerc.ui.vcl.UIInput;

public class UIDateDataStyle extends UIAbstractDataStyle {

    public UIDateDataStyle(UIDataStyle owner, DataCell data, boolean inGrid) {
        super(owner, data, inGrid);
    }

    @Override
    public String getText(String defaultText) {
        if (define != null && !define.readonly()) {
            var impl = Application.getBean(ImageConfigImpl.class);
            String dialogIcon;
            if (impl != null)
                dialogIcon = impl.getClassProperty(DateField.class, SummerUI.ID, "icon", "");
            else
                dialogIcon = DateField.config.getClassProperty("icon", "");
            UIComponent box = new UIComponent(null);
            box.setRootLabel("div");
            //
            UIInput input = new UIInput(box);
            input.setId(data.key());
            input.setValue(defaultText);
            input.setPlaceholder(define.placeholder());
            new UISelectDialog(box).setDialog("showDateDialog").setInputId(data.key()).setDialogIcon(dialogIcon);
            return box.toString();
        } else if (!owner.readonly()) {
            return getDisplayText(defaultText);
        }
        return defaultText;
    }

}
