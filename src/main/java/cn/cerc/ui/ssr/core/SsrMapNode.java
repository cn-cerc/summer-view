package cn.cerc.ui.ssr.core;

public class SsrMapNode extends SsrContainerNode {
    public static final SsrContainerSignRecord Sign = new SsrContainerSignRecord("map.begin", "map.end",
            (text) -> new SsrMapNode(text));

    public SsrMapNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrBlock block) {
        var params = block.getMap();
        if (params == null)
            return this.getText();

        var sb = new StringBuffer();

        var map = block.getMapProxy();
        map.first();
        while (map.fetch()) {
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
