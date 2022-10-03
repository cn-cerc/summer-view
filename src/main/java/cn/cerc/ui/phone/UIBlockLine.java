package cn.cerc.ui.phone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UIDataViewImpl;

public abstract class UIBlockLine extends UIComponent {
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

    public UIBlockLine addCell(String... fieldList) {
        var impl = findOwner(UIDataViewImpl.class);
        if (impl == null) {
            log.error("在 owner 中找不到 UIDataViewImpl");
            throw new RuntimeException("在 owner 中找不到 UIDataViewImpl");
        }
        var fields = impl.dataSet().fields();
        var dataStyle = impl.dataStyle();
        for (var fieldCode : fieldList) {
            FieldMeta column = fields.get(fieldCode);
            if (column == null)
                column = fields.add(fieldCode, FieldKind.Calculated);
            if (impl.active() && dataStyle != null)
                dataStyle.setDefault(column);
            createCell(fieldCode);
        }
        return this;
    }

    public abstract void createCell(String fieldCode);

}
