package cn.cerc.ui.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UIComponent implements IOriginOwner, Iterable<UIComponent> {
    private List<UIComponent> components = new ArrayList<>();
    private Object origin;
    private UIComponent owner;
    private String id;

    protected String cssClass;
    protected String cssStyle;

    public UIComponent() {
        super();
    }

    public UIComponent(UIComponent owner) {
        setOwner(owner);
        if (owner instanceof IOriginOwner)
            this.origin = ((IOriginOwner) owner).getOrigin();
    }

    @Deprecated
    public UIComponent(UIComponent owner, String id) {
        this.id = id;
        setOwner(owner);
    }

    public void addComponent(UIComponent component) {
        if (!components.contains(component)) {
            components.add(component);
        }
    }

    public final UIComponent getOwner() {
        return owner;
    }

    public void setOwner(UIComponent owner) {
        this.owner = owner;
        if (owner != null) {
            owner.addComponent(this);
        }
    }

    public final String getId() {
        return id;
    }

    public final UIComponent setId(String id) {
        this.id = id;
        if (owner != null && id != null)
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
        for (UIComponent component : this.getComponents())
            component.output(html);
    }

    public final String getCssClass() {
        return cssClass;
    }

    public UIComponent setCssClass(String cssClass) {
        this.cssClass = cssClass;
        return this;
    }

    public final String getCssStyle() {
        return cssStyle;
    }

    public UIComponent setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
        return this;
    }

    protected void outputCss(HtmlWriter html) {
        if (this.cssClass != null) {
            html.print(" class='%s'", cssClass);
        }
        if (this.cssStyle != null) {
            html.print(" style='%s'", cssStyle);
        }
    }

    @Override
    public final void setOrigin(Object parent) {
        this.origin = parent;
    }

    @Override
    public Object getOrigin() {
        return origin;
    }

    @Deprecated
    public <T> T create(Class<T> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        T obj = clazz.getDeclaredConstructor().newInstance();
        if (!(obj instanceof UIComponent)) {
            throw new RuntimeException("仅支持UIComponent及其子类，不支持创建类型: " + clazz.getName());
        }
        UIComponent item = (UIComponent) obj;
        item.setOwner(this);
        return obj;
    }

}
