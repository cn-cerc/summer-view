package cn.cerc.ui.grid;

import java.util.HashSet;
import java.util.LinkedHashSet;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
import cn.cerc.db.editor.EditorFactory;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.style.IGridStyle;

public class UIFastGrid extends UIComponent implements IGridStyle {
    private DataSet dataSet;
    private boolean active;
    private HashSet<FieldMeta> columns = new LinkedHashSet<>();

    public UIFastGrid(UIComponent owner) {
        super(owner);
        this.setRootLabel(isPhone() ? "div" : "table");
    }

    public UIFastGrid setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public FieldMeta addColumn(String fieldCode) {
        if (this.dataSet == null)
            throw new RuntimeException("dataSet is null");
        FieldMeta meta = dataSet.fields().get(fieldCode);
        if (meta == null)
            meta = dataSet.fields().add(fieldCode, FieldKind.Calculated);
        columns.add(meta);
        return meta;
    }

    @Override
    public void output(HtmlWriter html) {
        if (!this.active && this.dataSet != null) {
            if (columns.size() == 0) {
                for (var field : dataSet.fields())
                    columns.add(field);
            }
            if (this.isPhone()) {
                UIGridBody body = new UIGridBody(this, dataSet);
                for (var column : columns)
                    body.addColumn(column);
            } else {
                UIGridHead head = new UIGridHead(this);
                UIGridBody body = new UIGridBody(this, dataSet);
                for (var column : columns)
                    body.addColumn(column);
                head.addAll(this.columns);
            }
            this.active = true;
        }
        super.output(html);
    }

    public static void main(String[] args) {
        DataSet ds = new DataSet();
        ds.append();
        ds.setValue("code", "a01");
        ds.setValue("name", "jason");
        ds.setValue("sex", true);
        ds.append();
        ds.setValue("code", "a02");
        ds.setValue("name", "bade");
        ds.setValue("sex", false);

        ds.fields().get("code").setName("工号");
        ds.fields().get("name").setName("姓名");
        ds.fields().get("sex").setName("性别").onGetSetText(EditorFactory.ofBoolean("女的", "男的"));

        UIFastGrid grid = new UIFastGrid(null);
        grid.setPhone(false);
        grid.setDataSet(ds);
//        grid.addColumn("sex"); //指定栏位输出
        System.out.println(grid.toString());
        System.out.println(grid.toString());
    }

}
