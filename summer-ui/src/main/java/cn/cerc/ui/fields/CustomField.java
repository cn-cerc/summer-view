package cn.cerc.ui.fields;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class CustomField extends AbstractField {

    public CustomField(UIComponent dataView, String name, int width) {
        super(dataView, name, "_selectCheckBox_", width);
    }

    @Override
    public String getText() {
        if (buildText == null) {
            return "";
        }
        HtmlWriter html = new HtmlWriter();
        buildText.outputText(getCurrent(), html);
        return html.toString();
    }

}
