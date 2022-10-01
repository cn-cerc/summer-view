package cn.cerc.ui.phone;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIBlockGridCell extends UIBlockCell {
    private CellTypeEnum cellType = CellTypeEnum.Combo;

    public enum CellTypeEnum {
        OnlyTitle, OnlyValue, Combo;
    }

    public UIBlockGridCell(UIComponent owner) {
        super(owner);
        this.setRootLabel("td");
    }

    @Override
    public UIBlockGridCell setFieldCode(String fieldCode) {
        super.setFieldCode(fieldCode);
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        var dataSource = this.dataSource();
        var fieldCode = this.fieldCode();
        if (this.cellType != CellTypeEnum.OnlyTitle)
            this.setCssProperty("data-field", this.fieldCode());
        this.beginOutput(html);
        if (dataSource != null) {
            String name = dataSource.current().fields().get(fieldCode).name();
            switch (this.cellType) {
            case OnlyTitle:
                if (!Utils.isEmpty(name))
                    html.print(name);
                break;
            case OnlyValue:
                html.print(dataSource.current().getText(fieldCode));
                break;
            default:
                if (!Utils.isEmpty(name)) {
                    html.print(name);
                    html.print(":");
                }
                html.print(dataSource.current().getText(fieldCode));
            }
        } else
            html.print("dataSource is null");
        this.endOutput(html);
    }

    public CellTypeEnum cellType() {
        return cellType;
    }

    public UIBlockGridCell setCellType(CellTypeEnum cellType) {
        this.cellType = cellType;
        return this;
    }

}
