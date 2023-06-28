package cn.cerc.ui.style;

public class UIValueNode implements UISsrNodeImpl {

    private String text;

    @Override
    public String getText() {
        return text;
    }

    public UIValueNode(String text) {
        this.text = text;
    }

    @Override
    public String getSourceText() {
        return "${" + this.text + "}";
    }

}
