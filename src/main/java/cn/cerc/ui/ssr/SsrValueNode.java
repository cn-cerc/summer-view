package cn.cerc.ui.ssr;

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
    public String getHtml(ISsrBlock block) {
        var field = this.getField();
        return block == null ? "block is null" : block.getValue(field).orElse(this.getText());
    }
}
