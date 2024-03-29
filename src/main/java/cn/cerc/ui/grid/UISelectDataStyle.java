package cn.cerc.ui.grid;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.cerc.db.core.DataCell;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.UISelectDialog;
import cn.cerc.ui.vcl.UISelect;

public class UISelectDataStyle extends UIAbstractDataStyle {
    private Map<String, String> items = new LinkedHashMap<>();
    private String selected;

    public UISelectDataStyle(UIDataStyle owner, DataCell data, boolean inGrid) {
        super(owner, data, inGrid);
    }

    @Override
    public String getText(String defaultText) {
        if (define != null && !define.readonly()) {
            UIComponent box = new UIComponent(null);
            //
            UISelect input = new UISelect(box);
            input.setId(data.key());
            input.getOptions().putAll(items);
            input.setSelected(this.selected);
            //
            if (!Utils.isEmpty(define.dialog()))
                new UISelectDialog(box).setDialog(define.dialog()).setInputId(data.key());
            //
            return box.toString();
        } else if (!owner.readonly()) {
            return getDisplayText(defaultText);
        }
        return defaultText;
    }

    public void put(String key, String value) {
        items.put(key, value);
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

}
