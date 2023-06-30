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
        var params = this.getTemplate().getParams();
        var map = this.getTemplate().getMap();
        var dataRow = this.getTemplate().getDataRow();
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
        } else if (map != null || dataRow != null) {
            if (map != null && map.containsKey(field))
                return map.get(field);
            else if (dataRow != null && dataRow.exists(field)) {
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
