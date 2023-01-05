package cn.cerc.ui.form;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.cerc.db.core.DataRow;

public class FrmListStyle extends FormStyleDefine {
    protected Map<String, String> options = new LinkedHashMap<>();

    public FrmListStyle(String code, DataRow dataRow) {
        super(code, dataRow);
    }

    public FrmListStyle(String name, String code, DataRow data) {
        super(name, code, data);
    }

    public FrmListStyle(String name, String code, int width, DataRow dataRow) {
        super(name, code, width, dataRow);
    }

    public FrmListStyle setOptions(LinkedHashMap<String, String> options) {
        for (String key : options.keySet()) {
            String val = options.get(key);
            this.options.put(key, val);
        }
        return this;
    }

    public FrmListStyle put(String key, String name) {
        this.options.put(key, name);
        return this;
    }

    public FrmListStyle setEnum(Enum<?>[] items) {
        for (Enum<?> item : items) {
            options.put(String.valueOf(item.ordinal()), item.name());
        }
        return this;
    }

}
