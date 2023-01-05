package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;
import cn.cerc.ui.vcl.UIInput;

public class FormOptionStyle extends FrmListStyle {
    public FormOptionStyle(String code, DataRow data) {
        super(code, data);
        this.setWidth(12);
    }

    public FormOptionStyle(String name, String code, DataRow data) {
        super(name, code, 12, data);
    }

    public FormOptionStyle(String name, String code, int width, DataRow data) {
        super(name, code, width, data);
    }

    @Override
    public void buildHtml() {
        builder.append("<ul class='chooseList'>");
        for (String key : options.keySet()) {
            builder.append("<li>");
            String value = options.get(key);
            builder.append("<span class='optionSpan");
            if (dataRow.getString(code).equals(key))
                builder.append(" option_choosed");
            builder.append(String.format("' onclick='chooseOption(this)'>%s</span>", value));
            builder.append(String.format("<input type='%s' name='%s'", UIInput.TYPE_RADIO, code));
            if (dataRow.getString(code).equals(key))
                builder.append(" checked");
            builder.append(String.format(" value='%s' />", key));
            builder.append("</li>");
        }
        builder.append("</ul>");
    }

}
