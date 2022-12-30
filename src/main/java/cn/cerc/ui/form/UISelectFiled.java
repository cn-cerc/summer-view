package cn.cerc.ui.form;

import java.util.Map;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UISelectFiled extends UIListField {

    public UISelectFiled(UIComponent owner, String code) {
        super(owner, code);
    }

    public UISelectFiled(UIComponent owner, String code, String name) {
        super(owner, code, name);
    }

    public UISelectFiled(UIComponent owner, String code, String name, int width) {
        super(owner, code, name, width);
    }

    public UISelectFiled(UIComponent owner, String code, String name, int width, Map<String, String> options) {
        super(owner, code, name, width, options);
    }

    @Override
    public void writeContent(HtmlWriter html) {
        html.println("<select>");
        for (String key : getOptions().keySet()) {
            String value = getOptions().get(key);
            html.print("<option value=\"%s\"", value);
            if (this.current().getString(this.getCode()).equals(value))
                html.print(" selected");
            html.print(">%s</option>", key);
        }
        html.println("</select>");
    }

}
