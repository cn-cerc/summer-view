package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;
import cn.cerc.ui.vcl.UIInput;

public class FormRadioStyle extends FrmListStyle {
    public FormRadioStyle(String code, DataRow data) {
        super(code, data);
        this.setWidth(12);
    }

    public FormRadioStyle(String name, String code, DataRow data) {
        super(name, code, 12, data);
    }

    public FormRadioStyle(String name, String code, int width, DataRow data) {
        super(name, code, width, data);
    }

    @Override
    public void buildHtml() {
        builder.append("<div class='radioList'>");
        int index = 1;
        for (String key : options.keySet()) {
            String value = options.get(key);
            String id = code + index;
            builder.append(String.format("<input type='%s' name='%s'", UIInput.TYPE_RADIO, code));
            if (dataRow.getString(code).equals(key))
                builder.append(" checked");
            builder.append(String.format(" value='%s' id='%s' />", key, id));
            builder.append(String.format("<label for='%s'>%s</label>", id, value));
            index++;
        }
        builder.append("</div>");
    }

}
