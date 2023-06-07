package cn.cerc.ui.grid;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.db.editor.EditorFactory;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UIDataViewImpl;
import cn.cerc.ui.style.IGridStyle;
import cn.cerc.ui.vcl.UIForm.UIFormGatherImpl;
import cn.cerc.ui.vcl.UITd;
import cn.cerc.ui.vcl.UITh;
import cn.cerc.ui.vcl.UITr;

public class UIGridView extends UIComponent implements UIDataViewImpl, IGridStyle, UIFormGatherImpl {
    private static final Logger log = LoggerFactory.getLogger(UIGridView.class);
    private DataSet dataSet;
    private HashMap<String, FieldStyleDefine> items = new LinkedHashMap<>();
    private UIDataStyleImpl dataStyle;
    private boolean active;
    private boolean init;
    private UITr head;
    private UIGridBody body;

    private boolean columnItHidden;
    private int sumWidth = 0;

    public UIGridView(UIComponent owner) {
        super(owner);
        this.setRootLabel("table");
        this.setCssClass("dbgrid gridView");
        this.setActive(!this.isPhone());
    }

    public UIGridView setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    @Override
    public Optional<DataSet> getDataSet() {
        return Optional.ofNullable(dataSet);
    }

    public DataSet dataSet() {
        return dataSet;
    }

    @Override
    public UIDataStyleImpl dataStyle() {
        return this.dataStyle;
    }

    /**
     * 设置视图管理器的视图处理器
     * 
     * @param dataStyle 视图管理器
     * @return 返回视图管理器自身
     */
    public UIGridView setDataStyle(UIDataStyleImpl dataStyle) {
        if (dataStyle != null) {
            if (this.dataSet == null)
                this.setDataSet(dataStyle.getDataSet().orElse(null));

            dataStyle.setInGrid(true);
        }
        this.dataStyle = dataStyle;
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

    @Override
    public void output(HtmlWriter html) {
        if (this.dataStyle == null)
            throw new RuntimeException("dataStyle can not be null;");
        if (!this.active())
            return;
        if (!this.init && this.dataSet != null) {
            // 如果没有手动设置不展示序号列，则默认加上序号列
            if (!columnItHidden)
                this.dataStyle.addField("it");
            for (FieldStyleDefine styleDefine : this.dataStyle.fields().values()) {
                if (styleDefine != null && styleDefine.name() != null)
                    // 如果没设置默认取name长度
                    this.sumWidth += styleDefine.width() > 0 ? styleDefine.width() : styleDefine.name().length();
            }

            // 若没有指定列时，自动为所有列
            if (this.dataStyle.fields().size() == 0) {
                for (var field : dataSet.fields())
                    this.dataStyle.addField(field.code());
//                    fields.add(field);
            }
            // 建立相应的显示组件
            UITr head = head();
            UIGridBody body = body();
            // 先输出it
            if (!columnItHidden)
                outputCell(head, body, this.dataStyle.fields().get("it"));
            // 再输出非it
            for (var meta : this.dataStyle.fields().values()) {
                if (!meta.code().equals("it"))
                    outputCell(head, body, meta);
            }
            this.init = true;
        }
        super.output(html);
    }

    private void outputCell(UITr head, UIGridBody body, FieldStyleDefine styleDefine) {
        if (dataStyle != null)
            dataStyle.setDefault(styleDefine.field());
        String fieldName = styleDefine.name() == null ? styleDefine.code() : styleDefine.name();
        UITh th = new UITh(head).setText(fieldName);
        UITd td = new UITd(body);

        if (styleDefine != null && styleDefine.name() != null) {
            int width = styleDefine.width() > 0 ? styleDefine.width() : styleDefine.name().length();
            th.setCssProperty("onclick", "gridViewSort(this)");
            if (sumWidth > 0)
                th.setCssProperty("width", String.format("%f%%", Utils.roundTo((double) width / sumWidth * 100, -2)));
            if (!Utils.isEmpty(styleDefine.align())) {
                td.setCssProperty("align", styleDefine.align());
            }
        }

        new UIDataField(td).setField(styleDefine.code());
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

    @Override
    public int gatherRequest(HttpServletRequest request) {
        if (this.dataStyle == null)
            throw new RuntimeException("没有找到dataStyle");
        int total = 0;
        for (FieldStyleDefine field : this.dataStyle.fields().values()) {
            String fieldCode = field.code();
            String[] values = request.getParameterValues(fieldCode);
            boolean readonly = readonly();
            if (dataStyle != null)
                readonly = field.readonly();
            if (readonly)
                continue;
            // 开始更新
            if (values != null) {
                if (values.length != dataSet.size()) {
                    log.error("数据集的大小与收集到的数据，其数量不相符");
                    return 0;
                }
                for (int recNo = 0; recNo < values.length; recNo++) {
                    dataSet.setRecNo(recNo + 1);
                    dataSet.current().setValue(fieldCode, values[recNo]);
                }
            } else {
                for (var row : dataSet)
                    row.setValue(fieldCode, null);
            }
            total++;
        }
        return total++;

    }

    public boolean columnItHidden() {
        return columnItHidden;
    }

    public UIGridView setColumnItHidden(boolean columnItHidden) {
        this.columnItHidden = columnItHidden;
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

        UIGridView grid = new UIGridView(null).setDataSet(ds);
        grid.setDataStyle(new UIDataStyle());
        grid.setActive(true);
//        grid.addField("sex"); //指定栏位输出
        System.out.println(grid.toString());
    }

}
