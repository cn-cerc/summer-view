package cn.cerc.ui.ssr.core;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;

import org.springframework.context.annotation.Description;

import cn.cerc.mis.core.Application;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.page.IVuiEnvironment;

@Description("容器控件")
public abstract class VuiContainer<T> extends VuiControl {
    @Column
    protected static final boolean container = true;

    private Class<?> supportClass;

    @SuppressWarnings("unchecked")
    public VuiContainer() {
        super();
        supportClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    public VuiContainer(UIComponent owner) {
        super(owner);
        supportClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
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
            if (bean instanceof VuiComponent ssr)
                result.add(ssr.getClass());
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

}
