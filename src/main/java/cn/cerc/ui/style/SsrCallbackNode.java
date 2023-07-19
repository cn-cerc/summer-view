package cn.cerc.ui.style;

public class SsrCallbackNode extends SsrTextNode {
    public static final String FirstFlag = "callback";
    private SsrTemplateImpl template;
    private String field = null;

    public SsrCallbackNode(String text) {
        super(text);
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
        return "${" + super.getText() + "}";
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public String getHtml() {
        var callback = template.getCallback();
        if (callback != null && field != null) {
            var supplier = callback.get(field);
            if (supplier != null)
                return supplier.get();
        }
        return template.isStrict() ? this.getText() : "";
    }

    public static boolean is(String text) {
        return text.startsWith(FirstFlag);
    }

}
