package cn.cerc.ui.style;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UITemplateBlock extends UIComponent {
    private static final Logger log = LoggerFactory.getLogger(UITemplateBlock.class);
    protected SsrTemplateImpl template;

    public UITemplateBlock() {
        super(null);
    }

    public UITemplateBlock(UIComponent owner) {
        super(owner);
    }

    public UITemplateBlock(UIComponent owner, String templateText) {
        super(owner);
        this.template = new SsrTemplate(templateText);
    }

    public UITemplateBlock(UIComponent owner, Class<?> class1, String id) {
        super(owner);
        this.template = new SsrTemplate(class1, id);
    }

    /**
     * 不允许在此对象下再增加子对象
     */
    @Override
    public UIComponent addComponent(UIComponent child) {
        throw new RuntimeException("the is not container");
    }

    @Override
    public void output(HtmlWriter html) {
        if (template != null)
            html.print(template.getHtml());
        else
            log.warn("template is null");
    }

    public SsrTemplateImpl getTemplate() {
        Objects.requireNonNull(template);
        return template;
    }

    public void setTemplate(SsrTemplateImpl template) {
        this.template = template;
    }

}
