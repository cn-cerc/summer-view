package cn.cerc.ui.fields.editor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.ClassResource;
import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.fields.IFormatColumn;
import cn.cerc.ui.grid.lines.AbstractGridLine;
import cn.cerc.ui.grid.lines.MasterGridLine;

public class ColumnEditor {
    private static final ClassResource res = new ClassResource(ColumnEditor.class, SummerUI.ID);

    private AbstractField owner;
    private boolean init = false;
    private DataSet dataSet;
    private List<AbstractField> columns;
    private String onUpdate;
    private List<String> dataField = new ArrayList<>(); // 设置的字段列表
    private AbstractGridLine gridLine;

    public ColumnEditor(AbstractField owner) {
        this.owner = owner;

        UIComponent root = owner;
        while (root != null) {
            if (root instanceof AbstractGridLine) {
                gridLine = (AbstractGridLine) root;
                break;
            }
            root = root.getOwner();
        }
        if (gridLine == null)
            throw new RuntimeException(String.format(res.getString(1, "不支持的数据类型：%s"), owner.getClass().getName()));
    }

    public String getOnUpdate() {
        return onUpdate;
    }

    public void setOnUpdate(String onUpdate) {
        this.onUpdate = onUpdate;
    }

    public String format(DataRow ds) {
        String data = ds.getString(owner.getField());
        if (owner.getBuildText() != null) {
            HtmlWriter html = new HtmlWriter();
            owner.getBuildText().outputText(ds, html);
            data = html.toString();
        } else if (ds.getValue(owner.getField()) instanceof Double) {
            DecimalFormat df = new DecimalFormat("0.####");
            double value = ds.getDouble(owner.getField());
            data = df.format(new BigDecimal(Double.toString(value)));// 确保精度不丢失
        }

        if (!this.init) {
            dataSet = gridLine.dataSet();
            columns = new ArrayList<>();
            for (AbstractField field : gridLine.getFields()) {
                if (field instanceof IFormatColumn) {
                    if (field.readonly()) {
                        continue;
                    }
                    columns.add(field);
                }
            }
            this.init = true;
        }
        HtmlWriter html = new HtmlWriter();
        String inputStyle = "";
        html.print("<input");
        if (gridLine instanceof MasterGridLine) {
            html.print(" id='%s'", this.getDataId());
        } else {
            if (owner.getId() != null) {
                html.print(" id='%s'", owner.getId());
            }
            inputStyle = "width:80%;";
        }
        inputStyle += "border: 1px solid #dcdcdc;";
        html.print(" type='%s'", owner.getHtmType());
        html.print(" name='%s'", owner.getField());
        html.print(" value='%s'", data);
        html.print(" autocomplete='off'");
        html.print(" data-%s='[%s]'", owner.getField(), data);
        if (gridLine instanceof MasterGridLine) {
            html.print(" data-focus='[%s]'", this.getDataFocus());
            if (owner.getAlign() != null) {
                inputStyle += String.format("text-align:%s;", owner.getAlign());
            }
            if (owner.getOnclick() != null) {
                html.print(" onclick=\"%s\"", owner.getOnclick());
            } else {
                html.print(" onclick='this.select()'");
            }
        }
        if (!"".equals(inputStyle)) {
            html.print(" style='%s'", inputStyle);
        }
        html.print(" onkeydown='return tableDirection(event,this)'");
        if (dataField.size() > 0) {
            for (String field : dataField) {
                html.print(" data-%s='%s'", field, ds.getString(field));
            }
        }
        if (onUpdate != null) {
            html.print(" oninput=\"tableOnChanged(this,'%s')\"", onUpdate);
        } else {
            html.print(" oninput='tableOnChanged(this)'");
        }
        html.println("/>");
        return html.toString();
    }

    private String getDataId() {
        int recNo = dataSet.recNo();
        int colNo = columns.indexOf(owner);
        String selfId = String.format("%d_%d", recNo, colNo);
        return selfId;
    }

    private String getDataFocus() {
        int recNo = dataSet.recNo();
        int colNo = columns.indexOf(owner);

        String prior = recNo > 1 ? String.format("%d_%d", recNo - 1, colNo) : "0";
        String next = recNo < dataSet.size() ? String.format("%d_%d", recNo + 1, colNo) : "0";
        String left = colNo > 0 ? String.format("%d_%d", recNo, colNo - 1) : "0";
        String right = colNo < columns.size() - 1 ? String.format("%d_%d", recNo, colNo + 1) : "0";
        return String.format("\"%s\",\"%s\",\"%s\",\"%s\"", left, prior, right, next);
    }

    public AbstractGridLine getGridLine() {
        return gridLine;
    }

    public void setGridLine(AbstractGridLine gridLine) {
        this.gridLine = gridLine;
    }

    /**
     * 给元素设置data-*属性
     *
     * @return 要设置的字段列表
     */
    public List<String> getDataField() {
        return dataField;
    }
}
