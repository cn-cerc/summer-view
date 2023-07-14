package cn.cerc.ui.style;

public class SsrListNode extends SsrForeachNode {
    public static final ForeachSignRecord Sign = new ForeachSignRecord("list.begin", "list.end",
            (text) -> new SsrListNode(text));

    public SsrListNode(String text) {
        super(text);
    }

    @Override
    public String getHtml() {
        var list = this.getTemplate().getList();
        if (list == null)
            return this.getText();

        var sb = new StringBuffer();
        for (var param : list) {
            for (var item : this.getItems()) {
                if (item instanceof SsrValueNode value) {
                    if ("list.item".equals(item.getField()))
                        sb.append(param);
                    else
                        sb.append(value.getText());
                } else
                    sb.append(item.getText());
            }
        }
        return sb.toString();
    }

    @Override
    protected String getEndFlag() {
        return Sign.endFlag();
    }

}
