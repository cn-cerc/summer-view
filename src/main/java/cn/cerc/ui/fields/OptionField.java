package cn.cerc.ui.fields;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class OptionField extends AbstractField {

    private String defaultValue;
    private int size;// 默认显示行数
    private final Map<String, String> items = new LinkedHashMap<>();

    public OptionField(UIComponent owner, String name, String field) {
        super(owner, name, field, 0);
    }

    public OptionField(UIComponent owner, String name, String field, int width) {
        super(owner, name, field, width);
    }

    @Deprecated
    public OptionField add(String key, String text) {
        return this.put(key, text);
    }

    public OptionField put(String key, String text) {
        if (this.defaultValue == null) {
            defaultValue = key;
        }
        items.put(key, text);
        return this;
    }

    public OptionField copyValues(Map<String, String> items) {
        for (String key : items.keySet()) {
            this.put(key, items.get(key));
        }
        return this;
    }

    public OptionField copyValues(Enum<?>[] enums) {
        for (Enum<?> item : enums) {
            this.put(String.valueOf(item.ordinal()), item.name());
        }
        return this;
    }

    @Override
    public String getString() {
        String result = super.getString();
        if (result == null || "".equals(result)) {
            return this.defaultValue;
        }
        return result;
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        String current = this.getText();
        String id = this.getId();
        if (Utils.isEmpty(current) && !Utils.isEmpty(this.getValue())) {
            current = this.getValue();
        }
        html.print("<select id=\"%s\" name=\"%s\"", id, id);
        if (this.size > 0) {
            html.print(" size=\"%s\"", this.getSize());
        }
        if (this.readonly()) {
            html.print(" disabled");
        }
        if (this.getCssStyle() != null) {
            html.print(" style=\"%s\"", this.getCssStyle());
        }
        html.print(">");
        for (String key : items.keySet()) {
            String value = items.get(key);
            html.print("<option value=\"%s\"", key);
            if (key.equals(current)) {
                html.print(" selected");
            }
            html.print(">");
            html.println(String.format("%s</option>", value));
        }
        html.println("</select>");
        this.endOutput(html);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public OptionField setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public void updateField() {
        if (!this.readonly()) {
            super.updateField();
            if (this.defaultValue != null) {
                if (!this.current().hasValue(this.getField()))
                    this.current().setValue(this.getField(), this.defaultValue);
            }
        }
    }

}