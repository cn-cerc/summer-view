package cn.cerc.ui.phone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UIDataViewImpl;

public class UIBlockLine extends UIComponent {
    private static final Logger log = LoggerFactory.getLogger(UIBlockLine.class);

    public UIBlockLine(UIComponent owner) {
        super(owner);
        this.setRootLabel("div");
    }

    @Override
    public UIBlockLine addComponent(UIComponent child) {
        super.addComponent(child);
        return this;
    }

    public UIBlockLine addCell(String... fields) {
        var source = findOwner(UIDataViewImpl.class);
        if (source == null) {
            log.error("在 owner 中找不到 UIDataViewImpl");
            throw new RuntimeException("在 owner 中找不到 UIDataViewImpl");
        }
        var dataSet = source.dataSet();
        var dataStyle = source.dataStyle();
        for (var fieldCode : fields) {
            if (dataSet != null) {
                FieldMeta column = dataSet.fields().get(fieldCode);
                if (column == null)
                    column = dataSet.fields().add(fieldCode, FieldKind.Calculated);
                if (source.active() && dataStyle != null)
                    dataStyle.setDefault(column);
            }
            new UIBlockCell(this).setFieldCode(fieldCode);
        }
        return this;
    }

    public UIBlockCell getCell(int index) {
        return (UIBlockCell) this.getComponent(index);
    }

    public UIBlockLine addIt() {
        addComponent(new UIPhoneIt());
        return this;
    }

}
