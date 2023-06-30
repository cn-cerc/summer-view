package cn.cerc.ui.style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.Utils;

public class SsrValueNode implements SsrNodeImpl {
    private static final Logger log = LoggerFactory.getLogger(SsrValueNode.class);
    private SsrTemplateImpl template;
    private String text;

    @Override
    public String getField() {
        return text;
    }

    public SsrValueNode(String text) {
        this.text = text;
    }

    @Override
    public String getSourceText() {
        return "${" + this.text + "}";
    }
    
    @Override
    public String getValue() {
        var field = this.getField();
        var list = this.getTemplate().getList();
        var dataRow = this.getTemplate().getDataRow();
        if (Utils.isNumeric(field)) {
            if (list != null) {
                var index = Integer.parseInt(field);
                if (index >= 0 && index < list.size()) {
                    return list.get(index);
                } else {
                    log.error("not find index of list: {}", field);
                    return this.getSourceText();
                }
            } else {
                return this.getSourceText();
            }
        } else if (dataRow != null) {
            if (dataRow.exists(field)) {
                return dataRow.getText(field);
            } else {
                log.error("not find field: {}", field);
                return this.getSourceText();
            }
        } else
            return this.getSourceText();
    }

    protected SsrTemplateImpl getTemplate() {
        return template;
    }

    @Override
    public void setTemplate(SsrTemplateImpl template) {
        this.template = template;
    }
}
