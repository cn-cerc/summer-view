package cn.cerc.ui.form;

import java.util.Map;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIInput;

public class UIOptionField extends UIListField {
    public UIOptionField(UIComponent owner, String code) {
        super(owner, code);
    }

    public UIOptionField(UIComponent owner, String code, String name) {
        super(owner, code, name);
    }

    public UIOptionField(UIComponent owner, String code, String name, int width) {
        super(owner, code, name, width);
    }

    public UIOptionField(UIComponent owner, String code, String name, int width, Map<String, String> options) {
        super(owner, code, name, width, options);
    }

    @Override
    public void writeContent(HtmlWriter html) {
        html.print("<ul class='chooseList'>");
        for (String key : getOptions().keySet()) {
            html.print("<li>");
            String value = getOptions().get(key);
            html.print("<span class='optionSpan");
            if (this.current().getString(this.getCode()).equals(value))
                html.print(" option_choosed");
            html.print("' onclick='chooseOption(this)'>%s</span>", key);
            html.print("<input type='%s' name='%s'", UIInput.TYPE_RADIO, this.getCode());
            if (this.current().getString(this.getCode()).equals(value))
                html.print(" checked");
            html.print(" value='%s' />", value);
            html.println("</li>");
        }
        html.print("</ul>");
    }
}
