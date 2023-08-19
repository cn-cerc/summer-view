package cn.cerc.ui.ssr;

public class SsrCallbackNode extends SsrTextNode {
    public static final String FirstFlag = "callback";
    private String field = null;

    public SsrCallbackNode(String text) {
        super(text);
        var start = text.indexOf("(");
        var end = text.indexOf(")", start);
        if (start > -1 && end > -1)
            this.field = text.substring(start + 1, end);
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
    public String getHtml(ISsrBlock block) {
        var callback = block.getCallback();
        if (callback != null && field != null) {
            var supplier = callback.get(field);
            if (supplier != null)
                return supplier.get();
        }
        return block.strict() ? this.getText() : "";
    }

    public static boolean is(String text) {
        return text.startsWith(FirstFlag);
    }

}
