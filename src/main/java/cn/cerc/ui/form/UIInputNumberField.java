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
        html.print("<span onclick='reduceNumber(this, %s)'>－</span>", getStep());
        html.print("<input type='number' name='%s' value='%s' />", this.code, this.current().getString(this.code));
        html.print("<span onclick='increaseNumber(this, %s)'>+</span>", getStep());
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

    public int getStep() {
        return step;
    }

    public UIInputNumberField setStep(int step) {
        this.step = step;
        return this;
    }
}
