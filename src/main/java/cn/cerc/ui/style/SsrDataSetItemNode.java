package cn.cerc.ui.style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SsrDataSetItemNode extends SsrValueNode {
    private static final Logger log = LoggerFactory.getLogger(SsrDataSetItemNode.class);
    public static final String FirstFlag = "dataset.";

    public SsrDataSetItemNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrTemplateImpl template) {
        var field = this.getField().substring(FirstFlag.length(), this.getField().length());
        var dataSet = template.getDataSet();
        if (dataSet != null) {
            if (dataSet.exists(field))
                return dataSet.current().getText(field);
            else if (template.isStrict()) {
                log.error("not find field: {}", field);
                return this.getText();
            } else {
                return "";
            }
        } else {
            return this.getText();
        }
    }

    public static boolean is(String text) {
        return text.startsWith(FirstFlag) && !SsrDataSetRecNode.is(text) && !SsrDatasetNode.is(text)
                && !SsrDataSetRecNode.is(text);
    }

}
