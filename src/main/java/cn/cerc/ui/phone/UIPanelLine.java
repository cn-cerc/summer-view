package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSource;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.ui.core.UIComponent;

public class UIPanelLine extends UIBlockLine {

    private OnCreateCell onCreateCell;
    private DataSource source;

    public UIPanelLine(UIComponent owner) {
        super(owner);
    }

    @Override
    public UIPanelLine addCell(String... fieldList) {
        super.addCell(fieldList);
        return this;
    }

    public UIPanelCell getCell(int index) {
        return (UIPanelCell) this.getComponent(index);
    }

    @Override
    public void createCell(String fieldCode) {
        new UIPanelCell(this).setFieldCode(fieldCode);
        if (onCreateCell != null) {
            FieldMeta fieldMeta = source().dataSet().fields(fieldCode);
            onCreateCell.execute(this, fieldMeta);
        }
    }

    public interface OnCreateCell {
        void execute(UIPanelLine owner, FieldMeta fieldMeta);
    }

    public UIPanelLine onCreateCell(OnCreateCell onCreateCell) {
        this.onCreateCell = onCreateCell;
        return this;
    }

    public DataSource source() {
        if (source == null)
            source = findOwner(DataSource.class);
        return source;
    }

}
