package cn.cerc.ui.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import cn.cerc.mis.core.IForm;
import cn.cerc.mis.core.IOriginOwner;

public class UIComponent implements IOriginOwner, HtmlContent, Iterable<UIComponent> {
    private HashSet<UIComponent> components = new LinkedHashSet<>();
    private Map<String, Object> propertys = new HashMap<>();
    private UIComponent owner;
    private Object origin;
    private int phone = -1;
    private String rootLabel;

    public UIComponent(UIComponent owner) {
        super();
        if (owner != null) {
            this.setOwner(owner);
            this.setOrigin(owner.getOrigin());
            this.setPhone(owner.isPhone());
        }
    }

    public String getRootLabel() {
        return rootLabel;
    }

    public UIComponent setRootLabel(String rootLabel) {
        this.rootLabel = rootLabel;
        return this;
    }

    public UIComponent getOwner() {
        return owner;
    }

    public UIComponent setOwner(UIComponent owner) {
        if (this.owner == owner)
            return this;

        if (this.owner != null)
            this.owner.removeComponent(this);

        if (owner != null)
            owner.addComponent(this);

        return this;
    }

    public final HashSet<UIComponent> getComponents() {
        return components;
    }

    public int getComponentCount() {
        return components.size();
    }

    public void addComponent(UIComponent component) {
        if (component != null && !components.contains(component)) {
            component.owner = this;
            components.add(component);
        }
    }

    public void removeComponent(UIComponent component) {
        if (component != null) {
            component.owner = null;
            components.remove(component);
        }
    }

    @Override
    public UIComponent setOrigin(Object parent) {
        this.origin = parent;
        return this;
    }

    @Override
    public final Object getOrigin() {
        return origin;
    }

    @Override
    public final String toString() {
        HtmlWriter html = new HtmlWriter();
        output(html);
        return html.toString();
    }

    @Override
    public Iterator<UIComponent> iterator() {
        // 警告：此处不可直接返回 components.iterator
        List<UIComponent> items = new ArrayList<>();
        items.addAll(components);
        return items.iterator();
    }

    public final String getId() {
        return (String) propertys.get("id");
    }

    public final UIComponent setId(String id) {
        propertys.put("id", id);
        return this;
    }

    public final String getCssClass() {
        return (String) propertys.get("class");
    }

    public UIComponent setCssClass(String cssClass) {
        propertys.put("class", cssClass);
        return this;
    }

    public final String getCssStyle() {
        return (String) propertys.get("style");
    }

    public UIComponent setCssStyle(String cssStyle) {
        propertys.put("style", cssStyle);
        return this;
    }

    public UIComponent writeProperty(String key, Object value) {
        propertys.put(key, value);
        return this;
    }

    public final Object readProperty(String key) {
        return propertys.get(key);
    }

    protected Map<String, Object> getPropertys() {
        return propertys;
    }

    public final boolean isPhone() {
        if (phone == -1) {
            if (origin instanceof UIComponent)
                return ((UIComponent) origin).isPhone();
            else if (origin instanceof IForm)
                return ((IForm) origin).getClient().isPhone();
            else
                return false;
        } else {
            return phone == 1;
        }
    }

    public UIComponent setPhone(boolean value) {
        this.phone = value ? 1 : 0;
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        this.forEach(item -> item.output(html));
        this.endOutput(html);
    }

    protected void beginOutput(HtmlWriter html) {
        if (this.getRootLabel() == null || "".equals(getRootLabel()))
            return;
        html.print("<").print(getRootLabel());
        this.outputPropertys(html);
        html.print(">");
    }

    protected void endOutput(HtmlWriter html) {
        if (this.getRootLabel() == null || "".equals(getRootLabel()))
            return;
        html.print("</%s>", getRootLabel());
    }

    protected void outputPropertys(HtmlWriter html) {
        propertys.forEach((key, value) -> {
            if (key == null) {
                html.print(" %s", value);
            } else if (value != null && !"".equals(value)) {
                if (value instanceof Integer)
                    html.print(" %s=%s", key, value);
                else
                    html.print(" %s='%s'", key, value);
            }
        });
    }

    @Deprecated
    public final <T> T create(Class<T> clazz) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        T obj = clazz.getDeclaredConstructor().newInstance();
        if (!(obj instanceof UIComponent))
            throw new RuntimeException("仅支持UIComponent及其子类，不支持创建类型: " + clazz.getName());
        UIComponent item = (UIComponent) obj;
        item.setOwner(this);
        return obj;
    }

}
