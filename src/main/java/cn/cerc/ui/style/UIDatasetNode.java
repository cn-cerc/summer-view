package cn.cerc.ui.style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIDatasetNode extends UIForeachNode {
    private static final Logger log = LoggerFactory.getLogger(UIDatasetNode.class);
    public static final String StartFlag = "dataset.begin";
    public static final String EndFlag = "dataset.end";

    public UIDatasetNode(String text) {
        super(text);
    }

    @Override
    public String getValue(UITemplate dataSource) {
        var dataSet = dataSource.getDataSet();
        if (dataSet == null)
            return this.getSourceText();

        var sb = new StringBuffer();
        for (int i = 1; i <= dataSet.size(); i++) {
            var row = dataSet.records().get(i - 1);
            for (var item : this.getItems()) {
                if (item instanceof UIIfNode child) {
                    sb.append(child.getValue(row));
                } else if (item instanceof UIValueNode child) {
                   sb.append(child.getValue(dataSource));
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
