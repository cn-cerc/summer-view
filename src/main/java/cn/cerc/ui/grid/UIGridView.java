package cn.cerc.ui.grid;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
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
    private List<FieldMeta> fields = new ArrayList<>();
    private UIDataStyleImpl dataStyle;
    private boolean active;
    private boolean init;
    private UITr head;
    private UIGridBody body;
    private Map<String, String> alignMap = new LinkedHashMap<>();

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
                this.setDataSet(dataStyle.dataSet());
            for (var item : dataStyle.fields().values())
                fields.add(item.field());
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

    public FieldMeta addField(String fieldCode, String align) {
        return this.setAlign(fieldCode, align).addField(fieldCode);
    }

    public FieldMeta addFieldIt() {
        var dataSet = current().dataSet();
        if (dataSet == null) {
            log.error("没有找到dataSet");
            throw new RuntimeException("没有找到dataSet");
        }
        return this.addField("it", "center").onGetText(data -> "" + dataSet.recNo()).setName("序");
    }

    public UIGridView setAlign(String code, String align) {
        this.alignMap.put(code, align);
        return this;
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
                UITd td = new UITd(body);
                if (this.alignMap.size() != 0 && !Utils.isEmpty(this.alignMap.get(meta.code())))
                    td.setCssProperty("align", this.alignMap.get(meta.code()));
                new UIDataField(td).setField(meta.code());
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

    @Override
    public int gatherRequest(HttpServletRequest request) {
        int total = 0;
        for (FieldMeta field : fields) {
            String fieldCode = field.code();
            String[] values = request.getParameterValues(fieldCode);
            boolean readonly = readonly();
            if (dataStyle != null)
                readonly = dataStyle.fields().get(fieldCode).readonly();
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
