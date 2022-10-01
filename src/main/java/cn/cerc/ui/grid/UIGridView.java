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

public class UIGridView extends UIComponent implements IGridStyle {
    private DataSet dataSet;
    private boolean active;
    private HashSet<FieldMeta> columns = new LinkedHashSet<>();
    private UIOutputStyleImpl defaultStyle;

    public UIGridView(UIComponent owner) {
        super(owner);
        this.setRootLabel(isPhone() ? "div" : "table");
        this.setCssClass("dbgrid");
    }

    public UIGridView setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public FieldMeta addColumn(String fieldCode) {
        if (this.dataSet == null)
            throw new RuntimeException("dataSet is null");
        FieldMeta column = dataSet.fields().get(fieldCode);
        if (column == null)
            column = dataSet.fields().add(fieldCode, FieldKind.Calculated);
        columns.add(column);
        if (defaultStyle != null)
            column.onGetText(defaultStyle.getDefault(column));
        return column;
    }

    @Override
    public void output(HtmlWriter html) {
        if (!this.active && this.dataSet != null) {
            // 若没有指定列时，自动为所有列
            if (columns.size() == 0) {
                for (var column : dataSet.fields()) {
                    if (defaultStyle != null)
                        column.onGetText(defaultStyle.getDefault(column));
                    columns.add(column);
                }
            }
            // 根据不同的设备显示
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

    public UIOutputStyleImpl defaultStyle() {
        return this.defaultStyle;
    }

    public UIGridView setDefaultStyle(UIOutputStyleImpl outputStyle) {
        this.defaultStyle = outputStyle;
        return this;
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

        UIGridView grid = new UIGridView(null);
        grid.setPhone(false);
        grid.setDataSet(ds);
        grid.setDefaultStyle(new UIGridStyle());
//        grid.addColumn("sex"); //指定栏位输出
        System.out.println(grid.toString());
        System.out.println(grid.toString());
    }

    public FieldMeta addColumnIt() {
        return this.addColumn("it").onGetText(data -> "" + data.source().dataSet().recNo()).setName("序");
    }

}