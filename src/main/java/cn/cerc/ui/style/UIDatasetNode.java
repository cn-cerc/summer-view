package cn.cerc.ui.style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataSet;

public class UIDatasetNode extends UIForeachNode {
    private static final Logger log = LoggerFactory.getLogger(UIDatasetNode.class);
    public static final String StartFlag = "dataset.begin";
    public static final String EndFlag = "dataset.end";

    public UIDatasetNode(String text) {
        super(text);
    }

    public String getValue(DataSet params) {
        if (params == null)
            return this.getSourceText();

        var sb = new StringBuffer();
        for (int i = 1; i <= params.size(); i++) {
            var row = params.records().get(i - 1);
            for (var item : this.getItems()) {
                if (item instanceof UIIfNode child) {
                    sb.append(child.getValue(row));
                } else if (item instanceof UIValueNode child) {
                    if ("dataset.rec".equals(item.getText()))
                        sb.append(i);
                    else if (row.exists(item.getText()))
                        sb.append(row.getText(item.getText()));
                    else {
                        log.error("not find field: {}", item.getText());
                        sb.append(child.getSourceText());
                    }
                } else
                    sb.append(item.getSourceText());
            }
        }
        return sb.toString();
    }

    @Override
    protected String getEndFlag() {
        return EndFlag;
    }

}
