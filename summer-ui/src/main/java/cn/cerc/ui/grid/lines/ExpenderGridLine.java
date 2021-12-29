package cn.cerc.ui.grid.lines;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.grid.RowCell;

public class ExpenderGridLine extends AbstractGridLine {

    public ExpenderGridLine(UIComponent owner) {
        super(owner);
        this.setVisible(false);
    }

    @Override
    public void output(HtmlWriter html, int lineNo) {
        DataSet dataSet = current().dataSet();
        html.print("<tr");
        html.print(" id='%s_%s'", "tr" + dataSet.recNo(), lineNo);
        html.print(" role=\"%s\"", dataSet.recNo());
        if (!this.isVisible()) {
            html.print(" style=\"display:none\"");
        }
        html.println(">");
        for (RowCell item : this.getCells()) {
            AbstractField objField = item.getFields().get(0);
            html.print("<td");
            if (item.getColSpan() > 1) {
                html.print(" colspan=\"%d\"", item.getColSpan());
            }
            if (item.getStyle() != null) {
                html.print(" style=\"%s\"", item.getStyle());
            }
            if (item.getAlign() != null) {
                html.print(" align=\"%s\"", item.getAlign());
            } else if (objField.getAlign() != null) {
                html.print(" align=\"%s\"", objField.getAlign());
            }
            if (item.getRole() != null) {
                html.print(" role=\"%s\"", item.getRole());
            }

            html.print(">");
            for (AbstractField field : item.getFields()) {
                html.print("<span>");
                if (!"".equals(field.getName())) {
                    html.print(field.getName());
                    html.print(": ");
                }
                super.outputCell(html, field);
                html.println("</span>");
            }
            html.println("</td>");
        }
        html.println("</tr>");
    }

    @Override
    public UIComponent addComponent(UIComponent child) {
        if (child instanceof AbstractField) {
            AbstractField field = (AbstractField) child;
            getFields().add(field);

            RowCell cell;
            if (getCells().size() == 0) {
                cell = new RowCell(this);
                getCells().add(cell);
            } else {
                cell = getCells().get(0);
            }
            cell.addComponent(field);
        } else {
            super.addComponent(child);
        }
        return this;
    }

}
