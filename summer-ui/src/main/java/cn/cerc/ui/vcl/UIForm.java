package cn.cerc.ui.vcl;

import java.util.HashMap;
import java.util.Map;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIForm extends UIBaseHtml {
    private Map<String, String> items = new HashMap<>();
    private UIComponent top;
    private UIComponent bottom;

    public UIForm() {
        this(null, null);
    }

    public UIForm(UIComponent owner) {
        this(owner, null);
    }

    @Deprecated
    public UIForm(UIComponent owner, String id) {
        super(owner);
        this.setId(id);
        this.setRootLabel("form");
        this.writeProperty("method", "post");
    }

    public String getAction() {
        return (String) this.readProperty("action");
    }

    public void setAction(String action) {
        this.writeProperty("action", action);
    }

    public String getMethod() {
        return (String) this.readProperty("method");
    }

    public void setMethod(String method) {
        this.writeProperty("method", method);
    }

    public String getEnctype() {
        return (String) this.readProperty("enctype");
    }

    public void setEnctype(String enctype) {
        this.writeProperty("enctype", enctype);
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        super.beginOutput(html);
        if (top != null)
            html.println(top.toString());

        // 输出hidden 内容
        for (String key : items.keySet()) {
            String value = items.get(key);
            UIInput item = new UIInput(null);
            item.setHidden(true);
            item.setName(key);
            item.setId(key);
            item.setValue(value);
            html.println(item.toString());
        }
    }

    @Override
    public void endOutput(HtmlWriter html) {
        if (bottom != null)
            html.println(bottom.toString());
        super.endOutput(html);
    }

    public void addHidden(String key, String value) {
        items.put(key, value);
    }

    public Map<String, String> getItems() {
        return items;
    }

    public UIComponent getTop() {
        if (top == null) {
            top = new UIDiv(this);
            top.setOrigin(this.getOrigin());
            top.writeProperty("role", "top");
        }
        return top;
    }

    public UIComponent getBottom() {
        if (bottom == null) {
            bottom = new UIDiv();
            bottom.setOrigin(this.getOrigin());
            bottom.writeProperty("role", "bottom");
        }
        return bottom;
    }

}
