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

public class UIComponent implements IOriginOwner, Iterable<UIComponent> {
    private HashSet<UIComponent> components = new LinkedHashSet<>();
    private Map<String, Object> propertys = new HashMap<>();
    private UIComponent owner;
    private Object origin;
    private boolean phone;
    private String rootLabel;

    public UIComponent() {
        super();
    }

    public UIComponent(UIComponent owner) {
        super();
        setOwner(owner);
        if (owner != null) {
            this.setOrigin(owner.getOrigin());
            this.setPhone(owner.isPhone());
        }
    }

    @Deprecated
    public UIComponent(UIComponent owner, String id) {
        this(owner);
        setId(id);
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

    public final UIComponent getOwner() {
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

    @Override
    public final String toString() {
        HtmlWriter html = new HtmlWriter();
        output(html);
        return html.toString();
    }

    @Override
    public Iterator<UIComponent> iterator() {
        List<UIComponent> items = new ArrayList<>();
        items.addAll(components);
        return items.iterator();
    }

    public void output(HtmlWriter html) {
        this.beginOutput(html);
        this.forEach(item -> item.output(html));
        this.endOutput(html);
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

    @Override
    public UIComponent setOrigin(Object parent) {
        this.origin = parent;
        if (origin instanceof UIComponent)
            this.phone = ((UIComponent) origin).isPhone();
        else if (origin instanceof IForm)
            this.phone = ((IForm) origin).getClient().isPhone();
        return this;
    }

    @Override
    public final Object getOrigin() {
        return origin;
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
        return phone;
    }

    public UIComponent setPhone(boolean value) {
        this.phone = value;
        return this;
    }

    public String getRootLabel() {
        return rootLabel;
    }

    public UIComponent setRootLabel(String rootLabel) {
        this.rootLabel = rootLabel;
        return this;
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
