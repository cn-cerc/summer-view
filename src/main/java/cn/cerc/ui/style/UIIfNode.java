package cn.cerc.ui.style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataRow;

public class UIIfNode extends UIForeachNode {
    private static final Logger log = LoggerFactory.getLogger(UIIfNode.class);
    public static final String StartFlag = "if ";
    public static final String EndFlag = "endif";

    public UIIfNode(String text) {
        super(text);
    }

    public String getValue(DataRow dataRow) {
        var field = this.getText().substring(3, this.getText().length());
        if (!dataRow.exists(field)) {
            log.error("not find field: {}", field);
            return this.getSourceText();
        }
        if (!dataRow.getBoolean(field))
            return "";

        var sb = new StringBuffer();
        for (var item : this.getItems()) {
            if (item instanceof UIValueNode value) {
                field = value.getText();
                if (dataRow.exists(field))
                    sb.append(dataRow.getString(field));
                else
                    sb.append(value.getSourceText());
            } else
                sb.append(item.getSourceText());
        }
        return sb.toString();
    }

    @Override
    protected String getEndFlag() {
        return EndFlag;
    }

}
