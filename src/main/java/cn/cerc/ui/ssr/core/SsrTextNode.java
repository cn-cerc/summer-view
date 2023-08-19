package cn.cerc.ui.ssr.core;

public class SsrTextNode implements ISsrNode {
    private String text;

    public SsrTextNode(String text) {
        this.text = SsrUtils.fixSpace(text);
    }

    @Override
    public String getField() {
        return "";
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getHtml(SsrBlock block) {
        var val = this.text.trim();
        if (text.length() > 0 && "".equals(val))
            return " ";
        else
            return (text.startsWith(" ") ? " " : "") + val + (text.endsWith(" ") ? " " : "");
    }

}
