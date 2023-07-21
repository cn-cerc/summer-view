package cn.cerc.ui.fields;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.style.SsrTemplateImpl;

public class TemplateField extends AbstractField {
    private SsrTemplateImpl template;

    public TemplateField(UIComponent owner, String name, String field) {
        super(owner, name, field);
    }

    public TemplateField setTemplate(SsrTemplateImpl template) {
        this.template = template;
        return this;
    }

    public SsrTemplateImpl getTemplate() {
        return template;
    }

    @Override
    public String getText() {
        if (template != null) {
            template.setDataSet(this.getDataSet().orElse(null));
            return template.getHtml();
        } else
            return "template is null";
    }

}
