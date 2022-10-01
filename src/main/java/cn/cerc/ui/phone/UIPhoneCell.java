package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSource;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIPhoneCell extends UIComponent {
    private String fieldCode;

    public UIPhoneCell(UIComponent owner) {
        super(owner);
        this.setRootLabel("span");
    }

    public UIPhoneCell setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
        return this;
    }

    public String fieldCode() {
        return fieldCode;
    }

    @Override
    public void output(HtmlWriter html) {
        DataSource dataSource = dataSource();
        if (dataSource != null)
            this.setProperty("data-field", this.fieldCode);
        this.beginOutput(html);
        if (dataSource != null) {
            String name = dataSource.current().fields().get(fieldCode).name();
            String text = dataSource.current().getText(fieldCode);
            html.print(name);
            html.print(":");
            html.print(text);
        } else
            html.print("dataSource is null");
        this.endOutput(html);
    }

    protected DataSource dataSource() {
        DataSource result = null;
        UIComponent parent = this.getOwner();
        while (parent != null) {
            if (parent instanceof DataSource) {
                result = (DataSource) parent;
                break;
            }
            parent = parent.getOwner();
        }
        return result;
    }

}
