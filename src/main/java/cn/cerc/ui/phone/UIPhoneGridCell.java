package cn.cerc.ui.phone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataSetSource;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIPhoneGridCell extends UIPhoneCell {
    private static final Logger log = LoggerFactory.getLogger(UIPhoneGridCell.class);
    private CellTypeEnum cellType = CellTypeEnum.Combo;

    public enum CellTypeEnum {
        OnlyTitle,
        OnlyValue,
        Combo;
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
        var fieldCode = this.fieldCode();
        if (this.cellType != CellTypeEnum.OnlyTitle)
            this.setCssProperty("data-field", this.fieldCode());
        var impl = findOwner(DataSetSource.class);
        if (impl == null) {
            log.error("在 owner 中找不到 DataSource");
            throw new RuntimeException("在 owner 中找不到 DataSource");
        }
        this.beginOutput(html);
        String name = impl.getDataSet().map(ds -> ds.current()).orElseThrow().fields().get(fieldCode).name();
        switch (this.cellType) {
        case OnlyTitle:
            if (!Utils.isEmpty(name))
                html.print(name);
            break;
        case OnlyValue:
            html.print(impl.getDataSet().map(ds -> ds.current()).orElseThrow().getText(fieldCode));
            break;
        default:
            if (!Utils.isEmpty(name)) {
                html.print(name);
                html.print(":");
            }
            html.print(impl.getDataSet().map(ds -> ds.current()).orElseThrow().getText(fieldCode));
        }
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
