package cn.cerc.ui.grid.lines;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.grid.RowCell;

public class ChildGridLine extends AbstractGridLine {

    public ChildGridLine(UIComponent owner) {
        super(owner);
    }

    @Override
    public void output(HtmlWriter html, int lineNo) {
        html.print("<tr");
        html.print(" id='%s_%s'", "tr" + getCurrent().getDataSet().getRecNo(), lineNo);
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
            } else if (item.getFields().get(0).getField() != null) {
                html.print(" role=\"%s\"", item.getFields().get(0).getField());
            }

            html.print(">");
            for (AbstractField field : item.getFields()) {
                if (field.getName() != null && !"".equals(field.getName())) {
                    html.print("<span>%s：</span> ", field.getName());
                }
                super.outputCell(html, field);
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
            RowCell col;
            col = new RowCell(null);
            col.setAlign(field.getAlign());
            col.setRole(field.getField());
            getCells().add(col);
            col.addComponent(field);
        } else {
            super.addComponent(child);
        }
        return this;
    }

}
