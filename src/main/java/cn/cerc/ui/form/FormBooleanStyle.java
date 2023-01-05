package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.vcl.UIInput;

public class FormBooleanStyle extends FormStyleDefine {
    public FormBooleanStyle(String code, DataRow dataRow) {
        super(code, dataRow);
    }

    public FormBooleanStyle(String name, String code, DataRow dataRow) {
        super(name, code, dataRow);
    }

    public FormBooleanStyle(String name, String code, int width, DataRow dataRow) {
        super(name, code, width, dataRow);
    }

    @Override
    public String getHtml(int width) {
        builder.append(String.format("<div class='formEdit switchEdit' role='col%s'>", this.width));
        builder.append("<div>");
        if (!Utils.isEmpty(name)) {
            builder.append(String.format("<label for='%s'>", code));
            builder.append(String.format("%s</label>", name));
        }
        builder.append("<span class='switchSpan");
        if (dataRow.getBoolean(code))
            builder.append(" switch_checked");
        builder.append("' onclick='switchToogle(this)'></span>");
        builder.append(
                String.format("<input type='%s' name='%s' id='%s' value='1'", UIInput.TYPE_CHECKBOX, code, code));
        if (dataRow.getBoolean(code))
            builder.append(" checked");
        builder.append(" onchange='switchUpdate(this)' />");
        builder.append("</div>");
        if (this.dialog != null) {
            builder.append(String.format("<span class='dialogSpan' onclick='%s'>%s</span>", dialog.toString(),
                    dialog.getText()));
        }
        builder.append("</div>");
        return builder.toString();
    }
}
