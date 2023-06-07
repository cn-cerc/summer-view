package cn.cerc.ui.grid;

import java.util.List;

import cn.cerc.db.core.DataCell;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.UISelectDialog;
import cn.cerc.ui.vcl.UIInput;

public class UIBooleanDataStyle extends UIAbstractDataStyle {
    private String trueText = "是";
    private String falseText = "否";

    public UIBooleanDataStyle(UIDataStyle dataStyle, DataCell dataCell, boolean inGrid) {
        super(dataStyle, dataCell, inGrid);
        if (define.data() != null) {
            if (!(define.data() instanceof List<?>))
                throw new RuntimeException(String.format("%s StyleData.data 不为 List<String> 类型", data.key()));
            List<?> list = (List<?>) define.data();
            if (list.size() != 2)
                throw new RuntimeException(String.format("%s StyleData.data 的 list.size 不等于 2", data.key()));
            // 取出相应的文字说明
            this.setTrueText(list.get(0).toString());
            this.setFalseText(list.get(1).toString());
        }
    }

    @Override
    public String getText(String defaultText) {
        String result = data.getBoolean() ? trueText : falseText;
        if (define != null && !define.readonly()) {
            UIComponent box = new UIComponent(null);
            //
            UIInput input = new UIInput(box);
            input.setId(data.key());
            if (define.width() > 0) {
                String width = String.format("width: %dpx", define.width() * UIDataStyle.PX_SIZE);
                input.setCssStyle(width);
            } else
                input.setCssStyle(null);
            input.setPlaceholder(define.placeholder());
            input.setInputType(UIInput.TYPE_CHECKBOX);
            input.setChecked(data.getBoolean());
            // 允许外部更改input组件的属性
            define.output(input);
            //
            if (!Utils.isEmpty(define.dialog().getDialogfun()))
                new UISelectDialog(box).setDialog(define.dialog()).setInputId(data.key());
            //
            result = box.toString();
        } else if (!owner.readonly()) {
            result = getDisplayText(result);
        }
        return result;
    }

    public String trueText() {
        return trueText;
    }

    public UIBooleanDataStyle setTrueText(String trueText) {
        this.trueText = trueText;
        return this;
    }

    public String falseText() {
        return falseText;
    }

    public UIBooleanDataStyle setFalseText(String falseText) {
        this.falseText = falseText;
        return this;
    }

}
