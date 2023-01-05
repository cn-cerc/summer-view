package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;

public class FormSelectStyle extends FrmListStyle {
    public FormSelectStyle(String code, DataRow data) {
        super(code, data);
    }

    public FormSelectStyle(String name, String code, DataRow data) {
        super(name, code, data);
    }

    public FormSelectStyle(String name, String code, int width, DataRow data) {
        super(name, code, width, data);
    }

    @Override
    public void buildHtml() {
        builder.append(String.format("<select name='%s'>", code));
        for (String key : options.keySet()) {
            String value = options.get(key);
            builder.append(String.format("<option value=\"%s\"", key));
            if (dataRow.getString(code).equals(key))
                builder.append(" selected");
            builder.append(String.format(">%s</option>", value));
        }
        builder.append("</select>");
    }
}
