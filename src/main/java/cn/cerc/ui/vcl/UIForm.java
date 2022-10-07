package cn.cerc.ui.vcl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIForm extends UIComponent implements IHtml {
//    private static final Logger log = LoggerFactory.getLogger(UIForm.class);
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
        int gatherRequest(HttpServletRequest request);
    }

    public UIButton addSubmit(String buttonTitle) {
        var button = new UIButton(this).setText(buttonTitle);
        button.setValue("submit");
        button.setId("submit");
        return button;
    }

    /**
     * 收集所有提交的数据
     * 
     * @return 返回收集成功的笔数
     */
    public int gatherRequest() {
        return new UIFormGatherHelper(this, "submit").total();
    }

    public int gatherRequest(String submitId) {
        return new UIFormGatherHelper(this, submitId).total();
    }

}
