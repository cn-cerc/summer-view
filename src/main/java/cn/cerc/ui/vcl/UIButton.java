package cn.cerc.ui.vcl;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.INameOwner;
import cn.cerc.ui.core.UIComponent;

public class UIButton extends UIComponent implements IHtml, INameOwner {
    private String name;
    private String value;
    private String text;
    private String onclick;
    private String type;

    public UIButton(UIComponent owner) {
        super(owner);
    }

    public UIButton() {
        this(null);
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("<button");
        super.outputPropertys(html);
        if (name != null) {
            html.print(String.format(" name=\"%s\"", name));
        } else if (this.getId() != null) {
            html.print(String.format(" name=\"%s\"", getId()));
        }
        if (value != null) {
            html.print(String.format(" value=\"%s\"", value));
        }
        if (!Utils.isEmpty(this.getRole())) {
            html.print(" role='%s'", this.getRole());
        }
        if (type != null) {
            html.print(" type='%s'", this.type);
        }
        if (onclick != null) {
            html.print(String.format(" onclick=\"%s\"", onclick));
        }
        html.print(">");
        html.print(text);
        html.println("</button>");
    }

    public String getText() {
        return text;
    }

    public UIButton setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public UIButton setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public UIButton setValue(String value) {
        this.value = value;
        return this;
    }

    public String getOnclick() {
        return onclick;
    }

    public UIButton setOnclick(String onclick) {
        this.onclick = onclick;
        return this;
    }

    public UIButton setClickUrl(String url) {
        this.setOnclick(String.format("location.href='%s'", url));
        return this;
    }

    public String getType() {
        return type;
    }

    public UIButton setType(String type) {
        this.type = type;
        return this;
    }
}
