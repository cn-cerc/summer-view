package cn.cerc.ui.vcl.ext;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UILabel;

public class UIRadioButton extends UIComponent {
    private String name = "";
    private String value = "";
    private boolean isSelected = false;
    private UILabel label;

    public UIRadioButton(UIComponent owner) {
        super(owner);
    }

    @Override
    public void output(HtmlWriter html) {
        if (label != null) {
            label.output(html);
        }
        html.print("<input type='radio'");
        if (isSelected) {
            html.print(" checked=checked");
        }
        html.print(" name='%s' value='%s'", name, value);
        super.outputPropertys(html);
        html.println("/>");
    }

    public String getName() {
        return name;
    }

    public UIRadioButton setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public UIRadioButton setValue(String value) {
        this.value = value;
        return this;
    }

    public UILabel getLabel() {
        if (label == null) {
            label = new UILabel();
        }
        return label;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public UIRadioButton setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
        return this;
    }

}
