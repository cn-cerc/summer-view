package cn.cerc.ui.vcl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IForm;
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
        this.setCssProperty("method", "post");
    }

    public final String getAction() {
        return (String) this.getCssProperty("action");
    }

    public final UIForm setAction(String action) {
        this.setCssProperty("action", action);
        return this;
    }

    public final String getMethod() {
        return (String) this.getCssProperty("method");
    }

    public final void setMethod(String method) {
        this.setCssProperty("method", method);
    }

    public final String getEnctype() {
        return (String) this.getCssProperty("enctype");
    }

    public final void setEnctype(String enctype) {
        this.setCssProperty("enctype", enctype);
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
            top.setCssProperty("role", "top");
        }
        return top;
    }

    public UIComponent getBottom() {
        if (bottom == null) {
            bottom = new UIDiv();
            bottom.setOrigin(this.getOrigin());
            bottom.setCssProperty("role", "bottom");
        }
        return bottom;
    }

    public interface UIFormGatherImpl {
        /**
         * 允许一个组件收集多个字段的数据，正常情况下一个组件只会收集一个字段的数据
         * 
         * @param request HttpServletRequest
         * @return 返回成功收集的笔数
         */
        int gatherRequest(HttpServletRequest request);

    }

    /**
     * 收集所有提交的数据
     * 
     * @return 返回收集成功的笔数
     */
    public int gatherRequest() {
        if(this.getOrigin() instanceof IForm form)
            return new UIFormGatherHelper(this, form.getRequest()).total();
        else
            return 0;
    }

}
