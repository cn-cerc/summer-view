package cn.cerc.ui.phone;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIPhoneGridCell extends UIPhoneItem {
    private CellTypeEnum cellType = CellTypeEnum.Combo;

    public enum CellTypeEnum {
        OnlyTitle, OnlyValue, Combo;
    }

    public UIPhoneGridCell(UIComponent owner) {
        super(owner);
        this.setRootLabel("td");
    }

    @Override
    public UIPhoneGridCell setFieldCode(String fieldCode) {
        super.setFieldCode(fieldCode);
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        var dataSource = this.dataSource();
        var fieldCode = this.fieldCode();
        if (this.cellType != CellTypeEnum.OnlyTitle)
            this.setProperty("data-field", this.fieldCode());
        this.beginOutput(html);
        if (dataSource != null) {
            switch (this.cellType) {
            case OnlyTitle:
                html.print(dataSource.current().fields().get(fieldCode).name());
                break;
            case OnlyValue:
                html.print(dataSource.current().getText(fieldCode));
                break;
            default:
                html.print(dataSource.current().fields().get(fieldCode).name());
                html.print(": ");
                html.print(dataSource.current().getText(fieldCode));
            }
        } else
            html.print("dataSource is null");
        this.endOutput(html);
    }

    public CellTypeEnum cellType() {
        return cellType;
    }

    public UIPhoneGridCell setCellType(CellTypeEnum cellType) {
        this.cellType = cellType;
        return this;
    }

}
