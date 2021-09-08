package cn.cerc.ui.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UIComponent implements IOriginOwner, Iterable<UIComponent> {
    private List<UIComponent> components = new ArrayList<>();
    private Map<String, String> propertys = new HashMap<>();
    private UIComponent owner;
    private Object origin;

    public UIComponent() {
        super();
    }

    public UIComponent(UIComponent owner) {
        super();
        setOwner(owner);
        if (owner instanceof IOriginOwner)
            this.origin = ((IOriginOwner) owner).getOrigin();
    }

    @Deprecated
    public UIComponent(UIComponent owner, String id) {
        this(owner);
        setId(id);
    }

    public void addComponent(UIComponent component) {
        if (!components.contains(component))
            components.add(component);
    }

    public final UIComponent getOwner() {
        return owner;
    }

    public final UIComponent setOwner(UIComponent owner) {
        this.owner = owner;
        if (owner != null)
            owner.addComponent(this);
        return this;
    }

    public final List<UIComponent> getComponents() {
        return components;
    }

    @Override
    public final String toString() {
        HtmlWriter html = new HtmlWriter();
        output(html);
        return html.toString();
    }

    @Override
    public Iterator<UIComponent> iterator() {
        return components.iterator();
    }

    public void output(HtmlWriter html) {
        this.forEach(item -> item.output(html));
    }

    public final String getId() {
        return propertys.get("id");
    }

    public final UIComponent setId(String id) {
        propertys.put("id", id);
        return this;
    }

    public final String getCssClass() {
        return propertys.get("class");
    }

    public UIComponent setCssClass(String cssClass) {
        propertys.put("class", cssClass);
        return this;
    }

    public final String getCssStyle() {
        return propertys.get("style");
    }

    public UIComponent setCssStyle(String cssStyle) {
        propertys.put("style", cssStyle);
        return this;
    }

    protected final void appendPropertys(HtmlWriter html) {
        propertys.forEach((k, v) -> html.print(" %s='%s'", k, v));
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
