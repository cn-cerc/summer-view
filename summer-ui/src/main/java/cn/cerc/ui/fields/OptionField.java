package cn.cerc.ui.fields;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.cerc.core.Utils;
import cn.cerc.ui.core.HtmlWriter;
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

    public OptionField copyValues(Enum[] enums) {
        for (Enum item : enums) {
            this.put("" + item.ordinal(), item.name());
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
        html.print("<select id=\"%s\" name=\"%s\"", this.getId(), this.getId());
        if (this.size > 0) {
            html.print(" size=\"%s\"", this.getSize());
        }
        if (this.isReadonly()) {
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
        if (this.isShowStar()) {
            html.print("<font>*</font>");
        }
        html.print("<span></span>");
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}