package cn.cerc.ui.fields;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class RangeField extends AbstractField {

    public RangeField(UIComponent dataView, String name) {
        super(dataView, name, "_range_", 0);
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.hidden) {
            html.print("<input");
            html.print(" type=\"hidden\"");
            html.print(" name=\"%s\"", this.getId());
            html.print(" id=\"%s\"", this.getId());
            String value = this.getText();
            if (value != null) {
                html.print(" value=\"%s\"", value);
            }
            html.println("/>");
        } else {
            html.println("<label for=\"%s\">%s</label>", this.getId(), this.getName() + "：");
            AbstractField child = null;
            for (UIComponent component : this.getComponents()) {
                if (component instanceof AbstractField) {
                    if (child != null) {
                        html.print("-");
                    }
                    child = (AbstractField) component;
                    String val = child.getCSSClass_phone();
                    child.setCSSClass_phone("price");
                    child.outputInput(html);
                    child.setCSSClass_phone(val);
                }
            }
            if (this.dialog != null) {
                html.print("<span>");
                html.print("<a href=\"javascript:%s('%s')\">", this.dialog, this.getId());
                html.print("<img src=\"images/select-pic.png\">");
                html.print("</a>");
                html.print("</span>");
            } else {
                html.print("<span></span>");
            }
        }
    }

    @Override
    public void updateField() {
        AbstractField child = null;
        for (UIComponent component : this.getComponents()) {
            if (component instanceof AbstractField) {
                child = (AbstractField) component;
                child.updateField();
            }
        }
    }

}
