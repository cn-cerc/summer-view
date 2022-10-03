package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSource;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.ui.core.UIComponent;

public class UIPanelLine extends UIBlockLine {

    private DataSource source;
    private OnCreateCell onCreateCellBefore;
    private OnCreateCell onCreateCell;
    private OnCreateCell onCreateCellAfter;

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
        if (onCreateCellBefore != null) {
            FieldMeta fieldMeta = source().dataSet().fields(fieldCode);
            onCreateCellBefore.execute(this, fieldMeta);
        }
        if (onCreateCell != null) {
            FieldMeta fieldMeta = source().dataSet().fields(fieldCode);
            onCreateCell.execute(this, fieldMeta);
        } else {
            new UIPanelCell(this).setFieldCode(fieldCode);
        }
        if (onCreateCellAfter != null) {
            FieldMeta fieldMeta = source().dataSet().fields(fieldCode);
            onCreateCellAfter.execute(this, fieldMeta);
        }
    }

    public interface OnCreateCell {
        void execute(UIPanelLine owner, FieldMeta fieldMeta);
    }

    public UIPanelLine onCreateCellBefore(OnCreateCell onCreateCell) {
        this.onCreateCellBefore = onCreateCell;
        return this;
    }

    public UIPanelLine onCreateCell(OnCreateCell onCreateCell) {
        this.onCreateCell = onCreateCell;
        return this;
    }

    public UIPanelLine onCreateCellAfter(OnCreateCell onCreateCell) {
        this.onCreateCellAfter = onCreateCell;
        return this;
    }

    public DataSource source() {
        if (source == null)
            source = findOwner(DataSource.class);
        return source;
    }

}
