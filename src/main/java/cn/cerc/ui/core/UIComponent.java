package cn.cerc.ui.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.cerc.mis.core.HtmlContent;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IForm;
import cn.cerc.mis.core.IOriginOwner;

public class UIComponent implements IOriginOwner, HtmlContent, Iterable<UIComponent> {
    private List<UIComponent> components = new ArrayList<>();
    private Map<String, Object> propertys = new HashMap<>();
    private Set<String> signProperty = new HashSet<>();
    private UIComponent owner;
    private Object origin;
    private String rootLabel;
    private int phone = -1;
    // 是否为客户端渲染模式
    private int clientRender = -1;

    public UIComponent(UIComponent owner) {
        super();
        if (owner != null)
            owner.addComponent(this);
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
        if (this.owner != owner) {
            if (this.owner != null)
                this.owner.removeComponent(this);
            if (owner != null)
                owner.addComponent(this);
            this.owner = owner;
        }
        return this;
    }

    @Override
    public UIComponent setOrigin(Object origin) {
        this.origin = origin;
        return this;
    }

    @Override
    public final Object getOrigin() {
        return origin;
    }

    public final UIComponent getComponent(int index) {
        return this.components.get(index);
    }

    /**
     * 
     * @return 返回子组件列表
     */
    public final List<UIComponent> getComponents() {
        return this.components;
    }

    /**
     * @return 返回子组件的数量
     */
    public final int getComponentCount() {
        return components.size();
    }

    public UIComponent addComponent(UIComponent child) {
        if (child != null && !components.contains(child)) {
            components.add(child);
            child.registerOwner(this);
        }
        return this;
    }

    public UIComponent removeComponent(UIComponent component) {
        if (component != null) {
            components.remove(component);
            this.registerOwner(null);
        }
        return this;
    }

    protected void registerOwner(UIComponent owner) {
        this.owner = owner;
        if (owner == null)
            this.origin = null;
        else if (this.origin == null) {
            this.origin = owner.origin != null ? owner.origin : owner;
        }
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
        return new ArrayList<UIComponent>(components).iterator();
    }

    public final String getId() {
        return (String) propertys.get("id");
    }

    public UIComponent setId(String id) {
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

    public UIComponent setCssProperty(String key, Object value) {
        propertys.put(key, value);
        return this;
    }

    @Deprecated
    public final UIComponent writeProperty(String key, Object value) {
        return this.setCssProperty(key, value);
    }

    public final Object getCssProperty(String key) {
        return propertys.get(key);
    }

    @Deprecated
    public final Object readProperty(String key) {
        return this.getCssProperty(key);
    }

    protected Map<String, Object> getPropertys() {
        return propertys;
    }

    public UIComponent setPhone(boolean value) {
        this.phone = value ? 1 : 0;
        return this;
    }

    public final boolean isPhone() {
        if (this.phone == -1) {
            if (origin instanceof UIComponent)
                return ((UIComponent) origin).isPhone();
            else if (origin instanceof IForm)
                return ((IForm) origin).getClient().isPhone();
            else
                return false;
        } else {
            return this.phone == 1;
        }
    }

    public UIComponent setClientRender(boolean clientRender) {
        this.clientRender = clientRender ? 1 : 0;
        return this;
    }

    public final boolean isClientRender() {
        if (this.clientRender == -1) {
            if (this.owner != null)
                return owner.isClientRender();
            else
                return false;
        } else {
            return this.clientRender == 1;
        }
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        for (var item : this.components)
            item.output(html);
        this.endOutput(html);
    }

    public void beginOutput(HtmlWriter html) {
        if (this.getRootLabel() == null || "".equals(getRootLabel()))
            return;
        html.print("<").print(getRootLabel());
        this.outputPropertys(html);
        html.print(">");
    }

    public void endOutput(HtmlWriter html) {
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
        for (String sign : this.signProperty) {
            html.print(" ").print(sign);
        }
    }

    public final Set<String> getSignProperty() {
        return this.signProperty;
    }

    public UIComponent setSignProperty(String signValue, boolean value) {
        if (signValue != null && !"".equals(signValue)) {
            if (value)
                this.signProperty.add(signValue);
            else
                this.signProperty.remove(signValue);
        }
        return this;
    }

    public final boolean getSignProperty(String signValue) {
        return this.signProperty.contains(signValue);
    }

    @SuppressWarnings("unchecked")
    public <T> T findOwner(Class<T> clazz) {
        T result = null;
        UIComponent parent = this.getOwner();
        while (parent != null) {
            if (clazz.isInstance(parent)) {
                result = (T) parent;
                break;
            }
            parent = parent.getOwner();
        }
        return result;
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
