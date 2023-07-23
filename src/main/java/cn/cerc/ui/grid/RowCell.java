package cn.cerc.ui.grid;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.AbstractField;

public class RowCell extends UIComponent {

    public RowCell(UIComponent owner) {
        super(owner);
    }

    private int colSpan = 1;
    private String align;
    private String style;

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public void addField(AbstractField field) {
        this.addComponent(field);
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public List<AbstractField> getFields() {
        List<AbstractField> items = new ArrayList<>();
        for (UIComponent component : this) {
            if (component instanceof AbstractField)
                items.add((AbstractField) component);
        }
        return items;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Deprecated
    public String getFirstField() {
        return this.getFields().get(0).getField();
    }

}
