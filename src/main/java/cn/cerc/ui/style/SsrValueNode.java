package cn.cerc.ui.style;

public class SsrValueNode implements SsrNodeImpl {
//    private static final Logger log = LoggerFactory.getLogger(SsrValueNode.class);
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
    public String getHtml(SsrTemplateImpl template) {
        var field = this.getField();
        return template == null ? "template is null" : template.getValue(field).orElse(this.getText());
    }
}
