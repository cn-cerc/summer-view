package cn.cerc.ui.mvc;

import java.util.List;
import java.util.Map;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Datetime;
import cn.cerc.db.core.IUserLanguage;
import cn.cerc.db.core.Utils;
import cn.cerc.db.redis.RedisRecord;
import cn.cerc.mis.SummerMIS;
import cn.cerc.mis.cdn.CDN;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IForm;
import cn.cerc.mis.core.IPage;
import cn.cerc.mis.language.R;
import cn.cerc.ui.core.UIComponent;

//@Component
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class AbstractPage extends UIComponent implements IPage, IUserLanguage {
    protected static final ClassConfig config = new ClassConfig(AbstractPage.class, SummerMIS.ID);

    public AbstractPage(IForm owner) {
        super(null);
        this.setOrigin(owner);
    }

    @Override
    public final IForm getForm() {
        return (IForm) this.getOrigin();
    }

    @Override
    public final AbstractPage setOrigin(Object form) {
        super.setOrigin(form);
        if (form != null) {
            this.add("cdn", Application.getStaticPath());
            this.add("version", config.getString(CDN.BROWSER_CACHE_VERSION, "1.0.0.0"));
            this.put("jspPage", this);
            // 为兼容而设计
            this.add("summer_js", CDN.get(config.getString("static.js.summer", "js/summer.js")));
            this.add("myapp_js", CDN.get(config.getString("static.js.myapp", "js/myapp.js")));
        }
        return this;
    }

    @Override
    public UIComponent addComponent(UIComponent component) {
        if (component.getId() != null) {
            this.put(component.getId(), component);
        }
        super.addComponent(component);
        return this;
    }

    protected void put(String id, Object value) {
        getRequest().setAttribute(id, value);
    }

    public final String getMessage() {
        return getForm().getParam("message", null);
    }

    public final void setMessage(String message) {
        getForm().setParam("message", message);
    }

    @Override
    public void output(HtmlWriter html) {
        for (UIComponent component : this.getComponents()) {
            component.output(html);
        }
    }

    // 从请求或缓存读取数据
    public final String getValue(RedisRecord buff, String key) {
        String value = getRequest().getParameter(key);
        if (value != null) {
            value = value.trim();
            buff.setValue(key, value);
            return value;
        }

        value = buff.getString(key).replace("{}", "");
        if (Utils.isNumeric(value) && value.endsWith(".0"))
            value = value.substring(0, value.length() - 2);

        this.add(key, value);
        return value;
    }

    public void add(String id, String value) {
        getRequest().setAttribute(id, value);
    }

    public void add(String id, boolean value) {
        put(id, value);
    }

    public void add(String id, double value) {
        put(id, value);
    }

    public void add(String id, int value) {
        put(id, value);
    }

    public void add(String id, List<?> value) {
        put(id, value);
    }

    public void add(String id, Map<?, ?> value) {
        put(id, value);
    }

    public void add(String id, DataSet value) {
        put(id, value);
    }

    public void add(String id, DataRow value) {
        put(id, value);
    }

    public void add(String id, Datetime value) {
        put(id, value);
    }

    public void add(String id, UIComponent value) {
        put(id, value);
    }

    @Override
    public String getLanguageId() {
        return R.getLanguageId(getForm());
    }

}
