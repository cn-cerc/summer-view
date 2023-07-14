package cn.cerc.ui.style;

public class SsrTextNode implements SsrNodeImpl {
    private String text;

    public SsrTextNode(String text) {
        this.text = text.trim();
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
        return this.text;
    }

    @Override
    public void setTemplate(SsrTemplateImpl template) {
    }

}
