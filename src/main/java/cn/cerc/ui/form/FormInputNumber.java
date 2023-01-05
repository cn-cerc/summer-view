package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;

public class FormInputNumber extends FormStyleDefine {
    // 计步器
    private int step = 1;
    private String description;

    public FormInputNumber(String code, DataRow data) {
        super(code, data);
    }

    public FormInputNumber(String name, String code, DataRow data) {
        super(name, code, data);
    }

    public FormInputNumber(String name, String code, int width, DataRow data) {
        super(name, code, width, data);
    }

    @Override
    public void buildHtml() {
        builder.append("<div class='inputNumber'>");
        builder.append(String.format("<span onclick='reduceNumber(this, %s)'>－</span>", step));
        builder.append(String.format("<input type='number' name='%s' value='%s' />", code, dataRow.getString(code)));
        builder.append(String.format("<span onclick='increaseNumber(this, %s)'>+</span>", step));
        if (!Utils.isEmpty(description))
            builder.append(String.format("<span>%s</span>", description));
        builder.append("</div>");
    }

    public String getDescription() {
        return description;
    }

    public FormInputNumber setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getStep() {
        return step;
    }

    public FormInputNumber setStep(int step) {
        this.step = step;
        return this;
    }
}
