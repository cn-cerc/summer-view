package cn.cerc.ui.style;

public class SsrMapNode extends SsrContainerNode {
    public static final SsrContainerSignRecord Sign = new SsrContainerSignRecord("map.begin", "map.end",
            (text) -> new SsrMapNode(text));

    public SsrMapNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrTemplateImpl template) {
        var params = template.getMap();
        if (params == null)
            return this.getText();

        var sb = new StringBuffer();

        var map = template.getMapProxy();
        map.first();
        while (map.fetch()) {
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
