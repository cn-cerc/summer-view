package cn.cerc.ui.style;

public class SsrCallbackNode implements SsrNodeImpl {
    private SsrTemplateImpl template;
    private String text;
    private String field = null;

    public SsrCallbackNode(String text) {
        this.text = text;
        var start = text.indexOf("(");
        var end = text.indexOf(")", start);
        if (start > -1 && end > -1)
            this.field = text.substring(start + 1, end);
    }

    @Override
    public void setTemplate(SsrTemplateImpl template) {
        this.template = template;
    }

    @Override
    public String getText() {
        return "${" + this.text + "}";
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public String getHtml() {
        var callback = template.getCallback();
        if (callback != null && field != null)
            return callback.onGetHtml(field);
        else
            return this.getText();
    }

}
