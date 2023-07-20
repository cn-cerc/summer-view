package cn.cerc.ui.style;

public class SsrValueNode implements SsrNodeImpl {
//    private static final Logger log = LoggerFactory.getLogger(SsrValueNode.class);
    private SsrTemplateImpl template;
    private String text;

    public SsrValueNode(String text) {
        this.text = text;
    }

    @Override
    public String getField() {
        return text;
    }

    @Override
    public String getText() {
        return "${" + this.text + "}";
    }

    @Override
    public String getHtml() {
        var field = this.getField();
        var template = this.getTemplate();
        return template == null ? "template is null" : template.getValue(field).orElse(this.getText());
    }

    protected SsrTemplateImpl getTemplate() {
        return template;
    }

    @Override
    public void setTemplate(SsrTemplateImpl template) {
        this.template = template;
    }
}
