package cn.cerc.ui.style;

public class SsrListNode extends SsrContainerNode {
    public static final SsrContainerSignRecord Sign = new SsrContainerSignRecord("list.begin", "list.end",
            (text) -> new SsrListNode(text));

    public SsrListNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrTemplateImpl template) {
        var list = template.getListProxy();
        if (list == null) {
            if (!template.strict()) {
                return "";
            } else {
                return this.getText();
            }
        }

        list.first();
        var sb = new StringBuffer();
        while (list.fetch()) {
            for (var item : this.getItems())
                sb.append(item.getHtml(template));
        }
        return sb.toString();
    }

    @Override
    protected String getEndFlag() {
        return Sign.endFlag();
    }

}
