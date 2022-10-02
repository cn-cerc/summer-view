package cn.cerc.ui.phone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UIDataViewImpl;

public class UIPhoneLine extends UIComponent {
    private static final Logger log = LoggerFactory.getLogger(UIPhoneLine.class);

    public UIPhoneLine(UIComponent owner) {
        super(owner);
        this.setRootLabel("div");
    }

    @Override
    public UIPhoneLine addComponent(UIComponent child) {
        super.addComponent(child);
        return this;
    }

    public UIPhoneLine addCell(String... fields) {
        var impl = findOwner(UIDataViewImpl.class);
        if (impl == null) {
            log.error("在 owner 中找不到 UIDataViewImpl");
            throw new RuntimeException("在 owner 中找不到 UIDataViewImpl");
        }
        var dataSet = impl.dataSet();
        var dataStyle = impl.dataStyle();
        for (var fieldCode : fields) {
            if (dataSet != null) {
                FieldMeta column = dataSet.fields().get(fieldCode);
                if (column == null)
                    column = dataSet.fields().add(fieldCode, FieldKind.Calculated);
                if (impl.active() && dataStyle != null)
                    dataStyle.setDefault(column);
            }
            new UIPhoneCell(this).setFieldCode(fieldCode);
        }
        return this;
    }

    public UIPhoneCell getCell(int index) {
        return (UIPhoneCell) this.getComponent(index);
    }

    public UIPhoneLine addIt() {
        addComponent(new UIPhoneIt());
        return this;
    }

}
