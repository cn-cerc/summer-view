package cn.cerc.ui.style;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.Utils;

public class SsrValueNode implements SsrNodeImpl {
    private static final Logger log = LoggerFactory.getLogger(SsrValueNode.class);
    private SsrTemplateImpl template;
    private String text;

    public SsrValueNode(String text) {
        this.text = text;
    }

    @Override
    public String getField() {
        return text;
    }

    @Override
    public String getText() {
        return "${" + this.text + "}";
    }

    @Override
    public String getHtml() {
        var field = this.getField();
        var list = this.getTemplate().getList();
        if (Utils.isNumeric(field)) {
            if (list != null) {
                var index = Integer.parseInt(field);
                if (index >= 0 && index < list.size()) {
                    return list.get(index);
                } else if (this.getTemplate().isStrict()) {
                    log.error("not find index of list: {}", field);
                    return this.getText();
                } else {
                    return "";
                }
            } else {
                return this.getText();
            }
        } else {
            var map = this.getTemplate().getMap();
            var options = this.getTemplate().getOptions();
            var dataRow = this.getTemplate().getDataRow();
            var dataSet = this.getTemplate().getDataSet();
            if (map != null && map.containsKey(field)) {
                if (dataRow != null && dataRow.exists(field))
                    log.warn("map and dataRow exists field: {}", field);
                if (dataSet != null && dataSet.exists(field))
                    log.warn("map and dataSet exists field: {}", field);
                return map.get(field);
            } else if (dataRow != null && dataRow.exists(field)) {
                if (dataSet != null && dataSet.exists(field))
                    log.warn("dataRow and dataSet exists field: {}", field);
                return dataRow.getText(field);
            } else if (dataSet != null && dataSet.exists(field)) {
                var row = dataSet.currentRow();
                return row.isPresent() ? row.get().getText(field) : "";
            } else if (options != null && options.containsKey(field)) {
                return options.get(field);
            } else if (this.getTemplate().isStrict()) {
                log.error("not find field: {}", field);
                return this.getText();
            } else {
                return "";
            }
        }
    }

    protected SsrTemplateImpl getTemplate() {
        return template;
    }

    @Override
    public void setTemplate(SsrTemplateImpl template) {
        this.template = template;
    }
}
