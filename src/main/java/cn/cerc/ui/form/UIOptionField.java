package cn.cerc.ui.form;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIOptionField extends UIAbstractField {
    public Map<String, String> options = new LinkedHashMap<>();
    private Boolean inSelect;

    public UIOptionField(UIComponent owner, String code) {
        super(owner, code);
    }

    public UIOptionField(UIComponent owner, String code, String name) {
        super(owner, code, name);
    }

    public UIOptionField(UIComponent owner, String code, String name, int width) {
        super(owner, code, name, width);
    }

    public UIOptionField(UIComponent owner, String code, String name, int width, Map<String, String> options) {
        super(owner, code, name, width);
        this.options = options;
    }

    public UIOptionField(UIComponent owner, String code, String name, int width, Enum<?> enums) {
        super(owner, code, name, width);
        Enum<?>[] items = enums.getClass().getEnumConstants();
        for (Enum<?> item : items) {
            this.options.put(item.name(), "" + item.ordinal());
        }
    }

    @Override
    public void writeContent(HtmlWriter html) {
        html.print("UIOptionField not support.");
    }

    public boolean isInSelect() {
        return inSelect;
    }

    public void setInSelect(boolean inSelect) {
        this.inSelect = inSelect;
    }
}
