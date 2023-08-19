package cn.cerc.ui.ssr.core;

import java.lang.reflect.ParameterizedType;
import java.util.Set;

import javax.persistence.Column;

import org.springframework.context.annotation.Description;

import cn.cerc.ui.core.UIComponent;

@Description("容器控件")
public abstract class SsrContainer<T> extends SsrControl implements ISupportChildren {
    @Column
    protected static final boolean container = true;
    
    public SsrContainer() {
        super();
    }

    public SsrContainer(UIComponent owner) {
        super(owner);
    }

    @Override
    public Set<Class<? extends SsrComponent>> getChildren() {
        var result = this.getContainer().supplierClassList().getAttachClass(this.getClass());
        for (var clazz : SsrUtils.getClassList(this, this.defaultClass()).keySet())
            result.add(clazz);
        return result;
    }

    @Override
    public String getComponentSign() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<T> defaultClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
