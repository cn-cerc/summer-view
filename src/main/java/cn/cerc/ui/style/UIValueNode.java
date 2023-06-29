package cn.cerc.ui.style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.Utils;

public class UIValueNode implements UISsrNodeImpl {
    private static final Logger log = LoggerFactory.getLogger(UIValueNode.class);

    private String text;

    @Override
    public String getText() {
        return text;
    }

    public UIValueNode(String text) {
        this.text = text;
    }

    @Override
    public String getSourceText() {
        return "${" + this.text + "}";
    }

    public String getValue(UITemplate dataSource) {
        var field = this.getText();
        var params = dataSource.getParams();
        var dataRow = dataSource.getDataRow();
        if (Utils.isNumeric(field)) {
            if (params != null) {
                var index = Integer.parseInt(field);
                if (index >= 0 && index < params.length) {
                    return params[index];
                } else {
                    log.error("not find index: {}", field);
                    return this.getSourceText();
                }
            } else {
                return this.getSourceText();
            }
        } else if (dataRow != null) {
            if (dataRow.exists(field)) {
                return dataRow.getString(field);
            } else {
                log.error("not find field: {}", field);
                return this.getSourceText();
            }
        } else
            return this.getSourceText();
    }
}
