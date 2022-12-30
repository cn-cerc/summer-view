package cn.cerc.ui.form;

import java.util.Map;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIInput;

public class UIRadioField extends UIListField {
    public UIRadioField(UIComponent owner, String code) {
        super(owner, code);
    }

    public UIRadioField(UIComponent owner, String code, String name) {
        super(owner, code, name);
    }

    public UIRadioField(UIComponent owner, String code, String name, int width) {
        super(owner, code, name, width);
    }

    public UIRadioField(UIComponent owner, String code, String name, int width, Map<String, String> options) {
        super(owner, code, name, width, options);
    }

    @Override
    public void writeContent(HtmlWriter html) {
        html.print("<div class='radioList'>");
        int index = 1;
        for (String key : getOptions().keySet()) {
            String value = getOptions().get(key);
            String id = this.getCode() + index;
            html.print("<input type='%s' name='%s'", UIInput.TYPE_RADIO, this.getCode());
            if (this.current().getString(this.getCode()).equals(value))
                html.print(" checked");
            html.print(" value='%s' id='%s' />", value, id);
            html.print("<label for='%s'>%s</label>", id, key);
            index++;
        }
        html.print("</div>");
    }
}
