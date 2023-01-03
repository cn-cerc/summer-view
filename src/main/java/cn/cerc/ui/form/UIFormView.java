package cn.cerc.ui.form;

import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.cerc.db.core.DataRow;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIFormView extends UIComponent {
    private UIFormStyle formStyle = new UIFormStyle();
    private HashMap<String, FormStockStyle> items = new LinkedHashMap<>();

    public UIFormView(UIComponent owner) {
        super(owner);
        this.setRootLabel("form");
        this.setCssProperty("method", "post");
        this.setCssClass("customForm");
    }

    public UIFormView setFormStyle(UIFormStyle formStyle) {
        this.formStyle = formStyle;
        return this;
    }

    public FormStockStyle addStock(String caption) {
        FormStockStyle stockStyle = new FormStockStyle(caption, formStyle.dataRow());
        items.put(caption, stockStyle);
        return stockStyle;
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        if (formStyle.items.size() > 1) {
            for (FormStockStyle formStock : formStyle.items.values()) {
                for (FormLineStyleImpl lineStyle : formStock.items) {
                    html.print("<li role='col12'>");
                    for (FormStyleImpl formStyle : lineStyle.getList()) {
                        html.print(formStyle.getHtml());
                    }
                    html.print("</li>");
                }
            }
        }
        this.endOutput(html);
    }

    public static void main(String[] args) {

        UIFormView form = new UIFormView(null);
        UIFormStyle formStyle = new UIFormStyle();
        formStyle.setDataRow(DataRow.of("type_", "1"));
        formStyle.addStock("货物信息")
                .addLine(FormLineCol1Style.class)
                .addFormStyle(FormRadioStyle.class, "type_")
                .put("1", "娃哈哈")
                .put("2", "可口可乐");
        form.setFormStyle(formStyle);

        System.out.println(form.toString());
    }

}
