package cn.cerc.ui.grid;

import java.util.ArrayList;
import java.util.List;

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
    private List<FieldMeta> fields = new ArrayList<>();
    private UIDataStyleImpl dataStyle;
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
        return this.dataStyle;
    }

    @Override
    public UIGridView setDataStyle(UIDataStyleImpl style) {
        if (style != null) {
            if (this.dataSet == null)
                this.setDataSet(style.dataSet());
            for (var item : style.fields().values())
                fields.add(item.field());
        }
        this.dataStyle = style;
        return this;
    }

    @Override
    public boolean active() {
        return active;
    }

    /**
     * 
     * @param active 是否输出
     * @return 返回视图管理器
     */
    public UIGridView setActive(boolean active) {
        this.active = active;
        return this;
    }

    /**
     * 注册dataSet中的字段，若不存在则自动于dataSet中增加
     * 
     * @param fieldCode
     * @return 返回 dataSet.fields(fieldCode)
     */
    public FieldMeta addField(String fieldCode) {
        if (this.dataSet == null)
            throw new RuntimeException("dataSet is null");
        FieldMeta field = dataSet.fields(fieldCode);
        if (field == null)
            field = dataSet.fields().add(fieldCode, FieldKind.Calculated);
        fields.add(field);
        return field;
    }

    @Override
    public void output(HtmlWriter html) {
        if (!this.active())
            return;
        if (!this.init && this.dataSet != null) {
            // 若没有指定列时，自动为所有列
            if (fields.size() == 0) {
                for (var field : dataSet.fields())
                    fields.add(field);
            }
            // 建立相应的显示组件
            UITr head = head();
            UIGridBody body = body();
            for (var meta : fields) {
                if (dataStyle != null)
                    dataStyle.setDefault(meta);
                String fieldName = meta.name() == null ? meta.code() : meta.name();
                new UITh(head).setText(fieldName);
                new UIDataField(new UITd(body)).setField(meta.code());
            }
            this.init = true;
        }
        super.output(html);
    }

    /**
     * 
     * @return 返回表格输出时，tr的处理器
     */
    public UITr head() {
        if (head == null)
            this.head = new UITr(this);
        return this.head;
    }

    /**
     * 
     * @return 返回表格输入时，单身的处理器
     */
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

        UIGridView grid = new UIGridView(null).setDataSet(ds);
        grid.setDataStyle(new UIDataStyle());
        grid.setActive(true);
//        grid.addField("sex"); //指定栏位输出
        System.out.println(grid.toString());
    }

}
