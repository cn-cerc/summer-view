package cn.cerc.ui.ssr.core;

public class SsrListNode extends SsrContainerNode {
    public static final SsrContainerSignRecord Sign = new SsrContainerSignRecord("list.begin", "list.end",
            (text) -> new SsrListNode(text));

    public SsrListNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrBlock block) {
        var list = block.getListProxy();
        if (list == null) {
            if (!block.strict()) {
                return "";
            } else {
                return this.getText();
            }
        }

        list.first();
        var sb = new StringBuffer();
        while (list.fetch()) {
            for (var item : this.getItems())
                sb.append(item.getHtml(block));
        }
        return sb.toString();
    }

    @Override
    protected String getEndFlag() {
        return Sign.endFlag();
    }

}
