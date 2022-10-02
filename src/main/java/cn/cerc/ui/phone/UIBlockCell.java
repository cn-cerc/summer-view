package cn.cerc.ui.phone;

import cn.cerc.db.core.DataRowSourceImpl;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIBlockCell extends UIComponent {
    private String fieldCode;

    public UIBlockCell(UIComponent owner) {
        super(owner);
        this.setRootLabel("span");
    }

    public UIBlockCell setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
        return this;
    }

    public String fieldCode() {
        return fieldCode;
    }

    @Override
    public void output(HtmlWriter html) {
        DataRowSourceImpl dataSource = dataSource();
        if (dataSource != null)
            this.setCssProperty("data-field", this.fieldCode);
        this.beginOutput(html);
        if (dataSource != null) {
            String name = dataSource.current().fields().get(fieldCode).name();
            if (!Utils.isEmpty(name)) {
                html.print(name);
                html.print(":");
            }
            html.print(dataSource.current().getText(fieldCode));
        } else
            html.print("dataSource is null");
        this.endOutput(html);
    }

    protected DataRowSourceImpl dataSource() {
        DataRowSourceImpl result = null;
        UIComponent parent = this.getOwner();
        while (parent != null) {
            if (parent instanceof DataRowSourceImpl) {
                result = (DataRowSourceImpl) parent;
                break;
            }
            parent = parent.getOwner();
        }
        return result;
    }

}
