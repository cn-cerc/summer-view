package cn.cerc.ui.grid;

import java.util.HashSet;
import java.util.LinkedHashSet;

import cn.cerc.core.FieldMeta;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UITh;

public class UIGridHead extends UIComponent {
    private HashSet<UITh> columns = new LinkedHashSet<>();

    public UIGridHead(UIComponent owner) {
        super(owner);
        this.setRootLabel("tr");
    }

    public UIGridHead add(String name) {
        columns.add(new UITh(this).setText(name));
        return this;
    }

    public void addAll(HashSet<FieldMeta> list) {
        for (FieldMeta meta : list) {
            String name = meta.getName() == null ? meta.getCode() : meta.getName();
            columns.add(new UITh(this).setText(name));
        }
    }

}
