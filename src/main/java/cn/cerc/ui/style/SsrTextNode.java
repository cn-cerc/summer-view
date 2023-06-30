package cn.cerc.ui.style;

public class SsrTextNode implements SsrNodeImpl {
    private String text;

    @Override
    public String getField() {
        return "";
    }

    public SsrTextNode(String text) {
        this.text = text.trim();
    }

    @Override
    public String getSourceText() {
        return text;
    }

    @Override
    public void setTemplate(SsrTemplateImpl template) {
    }

    @Override
    public String getValue() {
        return this.text.trim();
    }

}
