package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSource;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIPhoneItem extends UIComponent {
    private DataSource dataSource;
    private String fieldCode;

    public UIPhoneItem(UIComponent owner) {
        super(owner);
        this.setRootLabel("span");
    }

    public UIPhoneItem setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
        return this;
    }

    public String fieldCode() {
        return fieldCode;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.dataSource != null)
            this.setProperty("data-field", this.fieldCode);
        this.beginOutput(html);
        if (dataSource != null)
            html.print(dataSource.current().getText(fieldCode));
        else
            html.print("dataSource is null");
        this.endOutput(html);
    }

    @Override
    protected void registerOwner(UIComponent owner) {
        super.registerOwner(owner);
        UIComponent parent = owner;
        while (parent != null) {
            if (parent instanceof DataSource) {
                dataSource = (DataSource) parent;
                break;
            }
            parent = parent.getOwner();
        }
    }

    public final DataSource dataSource() {
        return dataSource;
    }

}
