package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSource;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIPanelCell extends UIComponent {
    private String fieldCode;

    public UIPanelCell(UIComponent owner) {
        super(owner);
        this.setRootLabel("span");
    }

    public UIPanelCell setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
        return this;
    }

    public String fieldCode() {
        return fieldCode;
    }

    @Override
    public void output(HtmlWriter html) {
        var impl = findOwner(DataSource.class);
        if (impl != null)
            this.setCssProperty("data-field", this.fieldCode);
        this.beginOutput(html);
        if (impl != null) {
            String name = impl.dataSet().fields().get(fieldCode).name();
            if (!Utils.isEmpty(name)) {
                html.print(name);
                html.print(":");
            }
            html.print(impl.dataSet().current().getText(fieldCode));
        } else
            html.print("dataSource is null");
        this.endOutput(html);
    }

}