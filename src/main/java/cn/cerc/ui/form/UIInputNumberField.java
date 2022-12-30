package cn.cerc.ui.form;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIInputNumberField extends UIAbstractField {
    // 计步器
    private int step = 1;
    private String description;

    public UIInputNumberField(UIComponent owner, String code) {
        super(owner, code);
    }

    public UIInputNumberField(UIComponent owner, String code, String name) {
        super(owner, code, name);
    }

    public UIInputNumberField(UIComponent owner, String code, String name, int width) {
        super(owner, code, name, width);
    }

    @Override
    public void writeContent(HtmlWriter html) {
        html.print("<div class='inputNumber'>");
        html.print("<span onclick='reduceNumber(this, %s)'>－</span>", step);
        html.print("<input type='number' value='%s' />", this.current().getString(this.getCode()));
        html.print("<span onclick='increaseNumber(this, %s)'>+</span>", step);
        if (!Utils.isEmpty(description))
            html.print("<span>%s</span>", description);
        html.print("</div>");
    }

    public String getDescription() {
        return description;
    }

    public UIInputNumberField setDescription(String description) {
        this.description = description;
        return this;
    }
}
