package cn.cerc.ui.grid;

import java.util.HashSet;
import java.util.LinkedHashSet;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UITd;

public class UIGridBody extends UIComponent {
    private HashSet<FieldMeta> columns = new LinkedHashSet<>();
    private DataSet dataSet;

    public UIGridBody(UIComponent owner) {
        this(owner, null);
    }

    public UIGridBody(UIComponent owner, DataSet dataSet) {
        super(owner);
        this.dataSet = dataSet;
        this.setRootLabel(isPhone() ? "div" : "tr");
    }

    @Override
    public void output(HtmlWriter html) {
        dataSet.first();
        while (dataSet.fetch())
            super.output(html);
    }

    public final DataSet dataSet() {
        return dataSet;
    }

    public final UIGridBody setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public final HashSet<FieldMeta> columns() {
        return columns;
    }

    public FieldMeta addColumn(String field) {
        FieldMeta meta = dataSet.fields().get(field);
        if (meta == null)
            meta = dataSet.fields().add(field, FieldKind.Calculated);
        addColumn(meta);
        return meta;
    }

    public void addColumn(FieldMeta meta) {
        columns.add(meta);
        new UIDataComponent(new UITd(this), dataSet, meta.code());
    }

}
