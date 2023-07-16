package cn.cerc.ui.style;

public class SsrListNode extends SsrForeachNode {
    public static final ForeachSignRecord Sign = new ForeachSignRecord("list.begin", "list.end",
            (text) -> new SsrListNode(text));

    public SsrListNode(String text) {
        super(text);
    }

    @Override
    public String getHtml() {
        var list = this.getTemplate().getForeachList();
        if (list == null)
            return this.getText();

        list.reset();
        var sb = new StringBuffer();
        while (list.fetch()) {
            for (var item : this.getItems())
                sb.append(item.getHtml());
        }
        return sb.toString();
    }

    @Override
    protected String getEndFlag() {
        return Sign.endFlag();
    }

}
