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
        for (var key : params.keySet()) {
            var value = params.get(key);
            for (var item : this.getItems()) {
                if (item instanceof SsrValueNode child) {
                    if ("map.key".equals(item.getField()))
                        sb.append(key);
                    else if ("map.value".equals(item.getField()))
                        sb.append(value);
                    else
                        sb.append(child.getText());
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
