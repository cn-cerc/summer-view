package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.grid.UIOutputStyleImpl;

public class UIBlockLine extends UIComponent {

    public UIBlockLine(UIComponent owner) {
        super(owner);
        this.setRootLabel("div");
    }

    @Override
    public UIBlockLine addComponent(UIComponent child) {
        super.addComponent(child);
        return this;
    }

    public UIBlockCell addColumn(String fieldCode) {
        return new UIBlockCell(this).setFieldCode(fieldCode);
    }

    public UIBlockLine addColumns(String... fields) {
        DataSet dataSet = null;
        UIOutputStyleImpl defaultStyle = null;
        if (this.getOwner() instanceof UIBlockView view) {
            dataSet = view.dataSet();
            defaultStyle = view.defaultStyle();
        }
        for (var fieldCode : fields) {
            if (dataSet != null) {
                FieldMeta column = dataSet.fields().get(fieldCode);
                if (column == null)
                    column = dataSet.fields().add(fieldCode, FieldKind.Calculated);
                if (defaultStyle != null)
                    column.onGetText(defaultStyle.getDefault(column));
            }
            this.addColumn(fieldCode);
        }
        return this;
    }

    public UIBlockLine addIt() {
        addComponent(new UIBlockIt());
        return this;
    }

}
