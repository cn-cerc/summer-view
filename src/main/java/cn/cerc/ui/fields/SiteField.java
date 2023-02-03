package cn.cerc.ui.fields;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class SiteField extends AbstractField {

    private String defaultValue;
    private int size;// 默认显示行数
    private final Map<String, String> items = new LinkedHashMap<>();

    public SiteField(UIComponent owner, String name, String field) {
        super(owner, name, field, 0);
    }

    public SiteField(UIComponent owner, String name, String field, int width) {
        super(owner, name, field, width);
    }

    @Deprecated
    public SiteField add(String key, String text) {
        return this.put(key, text);
    }

    public SiteField put(String key, String text) {
        if (this.defaultValue == null) {
            defaultValue = key;
        }
        items.put(key, text);
        return this;
    }

    public SiteField copyValues(Map<String, String> items) {
        for (String key : items.keySet()) {
            this.put(key, items.get(key));
        }
        return this;
    }

    public SiteField copyValues(Enum<?>[] enums) {
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
        String current = this.getText();
        if (Utils.isEmpty(current) && !Utils.isEmpty(this.getValue())) {
            current = this.getValue();
        }
        html.println("<label for=\"%s\">%s</label>", this.getId(), this.getName() + "：");
        html.print("<div class=\"%s\">", this.getId());
        String initialValue = "";
        for (String key : items.keySet()) {
            if (key.equals(current))
                initialValue = items.get(key);
        }
        String placeholder = "";
        if (this.getPlaceholder() != null)
            placeholder = this.getPlaceholder();
        html.print("<input id=\"%s\" type=\"text\" name=\"%s\" value=\"%s\" placeholder=\"%s\" ", this.getId(),
                this.getId(), initialValue, placeholder);
        if (this.size > 0) {
            html.print(" size=\"%s\"", this.getSize());
        }
        if (this.readonly()) {
            html.print(" readonly");
        }
        if (this.getCssStyle() != null) {
            html.print(" style=\"%s\"", this.getCssStyle());
        }
        html.print("/><div>");
        for (String key : items.keySet()) {
            String value = items.get(key);
            html.print("<span value=\"%s\" >%s</span>", key, value);
        }
        html.println("</div></div>");
        if (this.isShowStar()) {
            html.print("<font>*</font>");
        }
        html.print("<span></span>");
        html.print("<script>SiteFieldInit(\"" + this.getId() + "\")</script>");
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

    public SiteField setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @Override
    public void updateField() {
        super.updateField();
        if (this.defaultValue != null) {
            if (!this.current().has(this.getField()))
                this.current().setValue(this.getField(), this.defaultValue);
        }
    }

}