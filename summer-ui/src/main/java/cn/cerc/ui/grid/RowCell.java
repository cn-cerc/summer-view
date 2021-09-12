package cn.cerc.ui.grid;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.AbstractField;

public class RowCell extends UIComponent {
    
    public RowCell(UIComponent owner) {
        super(owner);
    }

    private final List<AbstractField> fields = new ArrayList<>();
    private int colSpan = 1;
    private String align;
    private String role;
    private String style;

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public void addField(AbstractField field) {
        fields.add(field);
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public List<AbstractField> getFields() {
        return fields;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getFirstField() {
        return this.fields.get(0).getField();
    }
}
