package cn.cerc.ui.vcl;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UISelect extends UIComponent implements IHtml {
    private int size;
    private String name;
    private boolean readonly;
    private Map<String, String> options = new LinkedHashMap<>();
    private String selected;

    public UISelect(UIComponent owner) {
        super(owner);
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("<select ");
        if (this.getId() != null) {
            html.print("id=\"%s\"", this.getId());
        }
        html.print(" name=\"%s\"", this.getName());
        if (this.size > 0) {
            html.print(" size=\"%s\"", this.getSize());
        }
        if (this.isReadonly()) {
            html.print(" disabled");
        }
        this.outputPropertys(html);
        html.print(">");
        for (String key : options.keySet()) {
            String value = options.get(key);
            html.print("<option value=\"%s\"", key);
            if (key.equals(getSelected())) {
                html.print(" selected");
            }
            html.print(">");
            html.println(String.format("%s</option>", value));
        }
        html.println("</select>");
    }

    public int getSize() {
        return size;
    }

    public UISelect setSize(int size) {
        this.size = size;
        return this;
    }

    public String getName() {
        return name != null ? this.name : this.getId();
    }

    public UISelect setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public UISelect setReadonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public String getSelected() {
        return selected;
    }

    public UISelect setSelected(String selected) {
        this.selected = selected;
        return this;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public UISelect setOptions(Enum<?>[] enums) {
        for (Enum<?> item : enums)
            this.options.put(String.valueOf(item.ordinal()), item.name());
        return this;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public UISelect put(String key, String value) {
        this.options.put(key, value);
        return this;
    }

}
