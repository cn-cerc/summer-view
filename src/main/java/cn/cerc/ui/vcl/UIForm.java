package cn.cerc.ui.vcl;

import java.util.HashMap;
import java.util.Map;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIForm extends UIComponent implements IHtml {
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
        this.setProperty("method", "post");
    }

    public final String getAction() {
        return (String) this.getProperty("action");
    }

    public final void setAction(String action) {
        this.setProperty("action", action);
    }

    public final String getMethod() {
        return (String) this.getProperty("method");
    }

    public final void setMethod(String method) {
        this.setProperty("method", method);
    }

    public final String getEnctype() {
        return (String) this.getProperty("enctype");
    }

    public final void setEnctype(String enctype) {
        this.setProperty("enctype", enctype);
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
            top.setProperty("role", "top");
        }
        return top;
    }

    public UIComponent getBottom() {
        if (bottom == null) {
            bottom = new UIDiv();
            bottom.setOrigin(this.getOrigin());
            bottom.setProperty("role", "bottom");
        }
        return bottom;
    }

}
