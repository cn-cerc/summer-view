package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.grid.UIViewStyleImpl;

public class UIPhoneLine extends UIComponent {

    public UIPhoneLine(UIComponent owner) {
        super(owner);
        this.setRootLabel("div");
    }

    @Override
    public UIPhoneLine addChild(UIComponent child) {
        super.addChild(child);
        return this;
    }

    public UIPhoneCell addColumn(String fieldCode) {
        return new UIPhoneCell(this).setFieldCode(fieldCode);
    }

    public UIPhoneLine addColumns(String... fields) {
        DataSet dataSet = null;
        UIViewStyleImpl defaultStyle = null;
        if (this.getOwner() instanceof UIPhoneView view) {
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

    public UIPhoneLine addIt() {
        addChild(new UIPhoneIt());
        return this;
    }

}
