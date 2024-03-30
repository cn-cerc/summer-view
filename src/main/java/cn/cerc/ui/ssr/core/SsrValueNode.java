package cn.cerc.ui.ssr.core;

public class SsrValueNode implements ISsrNode {
    private final String text;

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
    public String getHtml(SsrBlock block) {
        String field = this.getField();
        return block == null ? "block is null" : block.getValue(field).orElse(this.getText());
    }
}
