package cn.cerc.ui.style;

public class SsrMapNode extends SsrForeachNode {
    public static final ForeachSignRecord Sign = new ForeachSignRecord("map.begin", "map.end",
            (text) -> new SsrMapNode(text));

    public SsrMapNode(String text) {
        super(text);
    }

    @Override
    public String getHtml() {
        var params = this.getTemplate().getMap();
        if (params == null)
            return this.getText();

        var sb = new StringBuffer();

        var map = this.getTemplate().getForeachMap();
        map.reset();
        while (map.fetch()) {
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
