package cn.cerc.ui.style;

public class SsrTextNode implements SsrNodeImpl {
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
    public String getHtml(SsrTemplateImpl template) {
        var val = this.text.trim();
        if (text.length() > 0 && "".equals(val))
            return " ";
        else
            return (text.startsWith(" ") ? " " : "") + val + (text.endsWith(" ") ? " " : "");
    }

}
