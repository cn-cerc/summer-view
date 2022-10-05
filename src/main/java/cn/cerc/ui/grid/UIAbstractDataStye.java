package cn.cerc.ui.grid;

import cn.cerc.db.core.DataCell;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIInput;

public abstract class UIAbstractDataStye {
    protected FieldStyleDefine define;
    protected DataCell data;
    protected boolean inGrid;
    protected UIDataStyle owner;

    public UIAbstractDataStye(UIDataStyle owner, DataCell data, boolean inGrid) {
        super();
        this.owner = owner;
        this.define = owner.items().get(data.key());
        this.data = data;
        this.inGrid = inGrid;
    }

    protected String getDisplayText(String text) {
        if (this.inGrid)
            return text;
        //
        UIComponent box = new UIComponent(null);
        UIInput input = new UIInput(box);
        input.setId(define.field().code());
        input.setValue(text);
        if (define.width() > 0) {
            String width = String.format("width: %dpx", define.width() * UIDataStyle.PX_SIZE);
            input.setCssStyle(width);
        } else
            input.setCssStyle(null);
        input.setReadonly(true);
        // 允许外部更改input组件的属性
        define.output(input);
        return box.toString();
    }

    public abstract String getText(String defaultText);

}
