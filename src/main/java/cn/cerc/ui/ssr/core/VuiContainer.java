package cn.cerc.ui.ssr.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;

import org.springframework.context.annotation.Description;

import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.page.IVuiEnvironment;

@Description("容器控件")
public abstract class VuiContainer<T> extends VuiControl {
    @Column
    protected static final boolean container = true;

    private Class<?> supportClass;

    public VuiContainer() {
        super();
        supportClass = findSupportClass(getClass());
    }

    public VuiContainer(UIComponent owner) {
        super(owner);
        supportClass = findSupportClass(getClass());
    }

    @SuppressWarnings("unchecked")
    private Class<T> findSupportClass(Class<?> clazz) {
        if (clazz.getGenericSuperclass() instanceof ParameterizedType type) {
            Type[] types = type.getActualTypeArguments();
            if (types != null && types.length > 0 && types[0] instanceof Class<?> result) {
                return (Class<T>) result;
            }
        }
        return findSupportClass(clazz.getSuperclass());
    }

    public Set<Class<? extends VuiComponent>> getChildren() {
        Set<Class<? extends VuiComponent>> result;
        IVuiEnvironment visualPage = this.canvas().environment();
        if (visualPage != null)
            result = visualPage.getAttachClass(this.getClass());
        else
            result = new LinkedHashSet<Class<? extends VuiComponent>>();

        var context = Application.getContext();
        context.getBeansOfType(supportClass).forEach((name, bean) -> {
            if (bean instanceof VuiComponent ssr) {
                Class<? extends VuiComponent> clazz = ssr.getClass();
                if (clazz.getAnnotation(VuiCommonComponent.class) != null)
                    result.add(clazz);
            }
        });
        return result;
    }

    public String getComponentSign() {
        return null;
    }

    protected VuiContainer<?> setSupportClass(Class<?> supportClass) {
        this.supportClass = supportClass;
        return this;
    }

    protected Class<?> getSupportClass() {
        return this.supportClass;
    }

    @SuppressWarnings("unchecked")
    protected <R> List<R> getCommponetsByClass(Class<R> clazz) {
        List<R> list = new ArrayList<>();
        for (UIComponent component : this) {
            if (clazz.isInstance(component))
                list.add((R) component);
        }
        return list;
    }

    @Override
    public void output(HtmlWriter html) {
        var props = this.properties();
        if (props.has("v_inner_align") && "Grid".equals(props.get("v_inner_align").asText())) {
            var cols = props.get("v_column_num").asInt();
            html.print("<div role='grid'>");
            int count = this.getComponentCount() / cols * cols;
            if (this.getComponentCount() % cols > 0) {
                count += cols;
            }
            for (int i = 0; i < count; i++) {
                if (i % cols == 0) {
                    html.print("<div role='grid-row'>");
                }
                html.print(String.format("<div role='grid-col'%s>",
                        props.has("v_inner_style") ? String.format(" style='%s'", props.get("v_inner_style").asText())
                                : ""));
                VuiComponent item = null;
                if (i < this.getComponentCount()) {
                    if (this.getComponent(i) instanceof VuiComponent temp)
                        item = temp;
                }
                if (item != null)
                    item.output(html);
                html.print("</div>");
                if (i % cols == cols - 1) {
                    html.print("</div>");
                }
            }
            html.println("</div>");
        } else
            super.output(html);
    }

}
