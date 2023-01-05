package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.mvc.AbstractPage;

public class UIFormView extends UIComponent {
    private UIFormStyle formStyle = new UIFormStyle();
    private String submit;
    private boolean readAll;
    private AbstractPage page;

    public UIFormView(UIComponent owner, AbstractPage page) {
        super(owner);
        this.page = page;
        this.setRootLabel("form");
        this.setCssProperty("method", "post");
        this.setCssClass("customForm");
    }

    public UIFormView setFormStyle(UIFormStyle formStyle) {
        this.formStyle = formStyle;
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        boolean bool = formStyle.items.size() > 1;
        if (bool)
            this.setCssClass("customForm groupForm");
        this.beginOutput(html);
        for (FormStockStyle formStock : formStyle.items.values()) {
            if (bool) {
                html.print("<div class='formGroup'>");
                html.print("<div class='groupTitle'>%s</div>", formStock.getCaption());
            }
            html.print("<ul>");
            for (FormLineStyleImpl lineStyle : formStock.items) {
                boolean isLists = lineStyle.getList().length > 1;
                for (FormStyleImpl formStyle : lineStyle.getList()) {
                    html.print("<li role='col%s'>", isLists ? formStyle.getWidth() : lineStyle.getWidth());
                    html.print(formStyle.getHtml(lineStyle.getWidth()));
                    html.print("</li>");
                }
            }
            html.print("</ul>");
            if (bool)
                html.print("</div>");
        }
        this.endOutput(html);
    }

    public String readAll() {
        if (readAll) {
            return submit;
        }

        submit = page.getRequest().getParameter("opera");
        for (FormStockStyle formStock : formStyle.items.values()) {
            for (FormLineStyleImpl lineStyle : formStock.items) {
                for (FormStyleImpl style : lineStyle.getList()) {
                    for (String code : style.getCodes()) {
                        this.updateValue(code);
                    }
                }
            }
        }

        readAll = true;
        return submit;
    }

    public void updateValue(String code) {
        String val = page.getRequest().getParameter(code);
        if (submit != null) {
            formStyle.dataRow().setValue(code, val);
        } else {
            if (val != null) {
                formStyle.dataRow().setValue(code, val);
            }
        }
    }

    public DataRow current() {
        if (this.formStyle == null)
            return new DataRow();
        return formStyle.current();
    }

}
