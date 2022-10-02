package cn.cerc.ui.grid;

import java.util.HashSet;
import java.util.LinkedHashSet;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
import cn.cerc.db.editor.EditorFactory;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UIDataViewImpl;
import cn.cerc.ui.style.IGridStyle;
import cn.cerc.ui.vcl.UITd;
import cn.cerc.ui.vcl.UITh;
import cn.cerc.ui.vcl.UITr;

public class UIGridView extends UIComponent implements UIDataViewImpl, IGridStyle {
    private DataSet dataSet;
    private HashSet<FieldMeta> fields = new LinkedHashSet<>();
    private UIDataStyleImpl viewStyle;
    private boolean active;
    private boolean init;
    private UITr head;
    private UIGridBody body;

    public UIGridView(UIComponent owner) {
        super(owner);
        this.setRootLabel("table");
        this.setCssClass("dbgrid");
        this.setActive(!this.isPhone());
    }

    @Override
    public UIGridView setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    @Override
    public DataSet dataSet() {
        return dataSet;
    }

    @Override
    public UIDataStyleImpl dataStyle() {
        return this.viewStyle;
    }

    @Override
    public UIGridView setDataStyle(UIDataStyleImpl style) {
        if (style != null) {
            if (this.dataSet == null)
                this.setDataSet(style.dataSet());
            for (var field : style.fields())
                this.addField(field.code());
        }
        this.viewStyle = style;
        return this;
    }

    @Override
    public boolean active() {
        return active;
    }

    @Override
    public UIGridView setActive(boolean active) {
        this.active = active;
        return this;
    }

    public FieldMeta addField(String fieldCode) {
        if (this.dataSet == null)
            throw new RuntimeException("dataSet is null");
        FieldMeta field = dataSet.fields().get(fieldCode);
        if (field == null)
            field = dataSet.fields().add(fieldCode, FieldKind.Calculated);
        fields.add(field);
        if (viewStyle != null)
            viewStyle.setDefault(field);
        return field;
    }

    @Override
    public void output(HtmlWriter html) {
        if (!this.active())
            return;
        if (!this.init && this.dataSet != null) {
            // 若没有指定列时，自动为所有列
            if (fields.size() == 0) {
                for (var field : dataSet.fields()) {
                    if (viewStyle != null)
                        viewStyle.setDefault(field);
                    fields.add(field);
                }
            }
            // 建立相应的显示组件
            UITr head = head();
            UIGridBody body = body();
            for (var meta : fields) {
                String fieldName = meta.name() == null ? meta.code() : meta.name();
                new UITh(head).setText(fieldName);
                new UIDataField(new UITd(body)).setField(meta.code());
            }
            this.init = true;
        }
        super.output(html);
    }

    public UITr head() {
        if (head == null)
            this.head = new UITr(this);
        return this.head;
    }

    public UIGridBody body() {
        if (body == null)
            this.body = new UIGridBody(this);
        return this.body;
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
        grid.setDataStyle(new UIDataStyle());
//        grid.addField("sex"); //指定栏位输出
        System.out.println(grid.toString());
    }

}
