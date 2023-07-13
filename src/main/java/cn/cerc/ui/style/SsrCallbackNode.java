package cn.cerc.ui.style;

public class SsrCallbackNode implements SsrNodeImpl {
    public static final String FirstFlag = "callback";
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
        if (this.template != null) {
            var callback = this.template.getCallback();
            if (callback != null)
                return callback.onGetHtml(this);
        }
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
            return callback.onGetHtml(this);
        else
            return this.getText();
    }

    public static boolean is(String text) {
        return text.startsWith(FirstFlag);
    }

}
