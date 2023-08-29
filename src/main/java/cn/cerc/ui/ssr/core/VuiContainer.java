package cn.cerc.ui.ssr.core;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashSet;
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

    @SuppressWarnings("unchecked")
    public VuiContainer() {
        super();
        if (getClass().getGenericSuperclass() instanceof ParameterizedType type) {
            supportClass = (Class<T>) type.getActualTypeArguments()[0];
        } else if (getClass().getSuperclass().getGenericSuperclass() instanceof ParameterizedType type) {
            supportClass = (Class<T>) type.getActualTypeArguments()[0];
        }
    }

    @SuppressWarnings("unchecked")
    public VuiContainer(UIComponent owner) {
        super(owner);
        if (getClass().getGenericSuperclass() instanceof ParameterizedType type) {
            supportClass = (Class<T>) type.getActualTypeArguments()[0];
        } else if (getClass().getSuperclass().getGenericSuperclass() instanceof ParameterizedType type) {
            supportClass = (Class<T>) type.getActualTypeArguments()[0];
        }
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

    @Override
    public void output(HtmlWriter html) {
        var props = this.properties();
        if (props.has("v_inner_align") && "Grid".equals(props.get("v_inner_align").asText())) {
            var rows = props.get("v_row_num").asInt();
            var cols = props.get("v_column_num").asInt();
            var index = 0;
            html.print("<div role='grid'>");
            for (var i = 0; i < cols; i++) {
                html.print("<div role='grid-row'>");
                for (var j = 0; j < rows; j++) {
                    html.print(String.format("<div role='grid-col' style='%s'>", props.get("v_style").asText()));
                    VuiComponent item = null;
                    if (index < this.getComponentCount()) {
                        if (this.getComponent(index) instanceof VuiComponent temp)
                            item = temp;
                    }
                    if (item != null)
                        item.output(html);
                    html.print("</div>");
                    index++;
                }
                html.println("</div>");
            }
            html.println("</div>");
        } else
            super.output(html);
    }

}
