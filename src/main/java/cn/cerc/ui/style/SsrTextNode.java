package cn.cerc.ui.style;

public class SsrTextNode implements SsrNodeImpl {
    private String text;

    public SsrTextNode(String text) {
        this.text = text;
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
    public String getHtml() {
        var val = this.text.trim();
        return (text.startsWith(" ") ? " " : "") + val + (text.endsWith(" ") ? " " : "");
    }

    @Override
    public void setTemplate(SsrTemplateImpl template) {
    }

}
