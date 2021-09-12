package cn.cerc.ui.grid.lines;

import java.util.List;

import cn.cerc.core.DataSet;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.grid.IColumnsManager;
import cn.cerc.ui.grid.RowCell;

public class MasterGridLine extends AbstractGridLine {
    //
    private String primaryKey;
    // 列管理器，用于支持自定义栏位
    private IColumnsManager manager;

    public MasterGridLine(UIComponent owner) {
        super(owner);
    }

    @Override
    public void output(HtmlWriter html, int lineNo) {
        DataSet dataSet = getCurrent().getDataSet();
        html.print("<tr");
        html.print(" id='%s'", "tr" + dataSet.getRecNo());
        if (this.getPrimaryKey() != null) {
            html.print(" data-rowid='%s'", dataSet.getString(this.getPrimaryKey()));
        }
        html.println(">");
        for (RowCell cell : this.getOutputCells()) {
            AbstractField field = cell.getFields().get(0);
            html.print("<td");
            if (cell.getColSpan() > 1) {
                html.print(" colspan=\"%d\"", cell.getColSpan());
            }
            if (cell.getStyle() != null) {
                html.print(" style=\"%s\"", cell.getStyle());
            } else if (field.getWidth() == 0) {
                html.print(" style=\"%s\"", "display:none");
            }

            if (cell.getAlign() != null) {
                html.print(" align=\"%s\"", cell.getAlign());
            } else if (field.getAlign() != null) {
                html.print(" align=\"%s\"", field.getAlign());
            }

            if (cell.getRole() != null) {
                html.print(" role=\"%s\"", cell.getRole());
            } else if (field.getField() != null) {
                html.print(" role=\"%s\"", field.getField());
            }
            html.print(">");

            field.outputOfGridLine(html);
            html.println("</td>");
        }
        html.println("</tr>");
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public void addComponent(UIComponent child) {
        if (child instanceof AbstractField) {
            AbstractField field = (AbstractField) child;
            getFields().add(field);

            RowCell cell = new RowCell(null);
            getCells().add(cell);

            cell.setAlign(field.getAlign());
            cell.setRole(field.getField());
            cell.addField(field);
        } else {
            super.addComponent(child);
        }
    }

    public IColumnsManager getManager() {
        return manager;
    }

    public void setManager(IColumnsManager manager) {
        this.manager = manager;
    }

    public List<RowCell> getOutputCells() {
        if (this.manager == null) {
            return getCells();
        }
        return manager.Reindex(super.getCells());
    }

}
