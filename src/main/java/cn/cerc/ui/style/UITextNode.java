package cn.cerc.ui.style;

public class UITextNode implements UISsrNodeImpl {
    private String text;

    @Override
    public String getText() {
        return text;
    }

    public UITextNode(String text) {
        this.text = text.trim();
    }

    @Override
    public String getSourceText() {
        return text;
    }

}
