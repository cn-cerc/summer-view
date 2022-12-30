package cn.cerc.ui.form;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIListField extends UIAbstractField {
    private Map<String, String> options = new LinkedHashMap<>();
    private Boolean inSelect;

    public UIListField(UIComponent owner, String code) {
        super(owner, code);
    }

    public UIListField(UIComponent owner, String code, String name) {
        super(owner, code, name);
    }

    public UIListField(UIComponent owner, String code, String name, int width) {
        super(owner, code, name, width);
    }

    public UIListField(UIComponent owner, String code, String name, int width, Map<String, String> options) {
        super(owner, code, name, width);
        this.setOptions(options);
    }

    public UIListField(UIComponent owner, String code, String name, int width, Enum<?> enums) {
        super(owner, code, name, width);
        this.setEnum(enums);
    }

    @Override
    public void writeContent(HtmlWriter html) {
        html.print("UIOptionField not support.");
    }

    public boolean isInSelect() {
        return inSelect;
    }

    public UIListField setInSelect(boolean inSelect) {
        this.inSelect = inSelect;
        return this;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public UIListField setOptions(Map<String, String> options) {
        this.options = options;
        return this;
    }

    public UIListField setEnum(Enum<?> enums) {
        Enum<?>[] items = enums.getClass().getEnumConstants();
        for (Enum<?> item : items) {
            this.getOptions().put(item.name(), "" + item.ordinal());
        }
        return this;
    }
}
