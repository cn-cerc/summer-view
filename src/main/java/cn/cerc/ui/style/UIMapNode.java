package cn.cerc.ui.style;

public class UIMapNode extends UIForeachNode {
    public static final String StartFlag = "map.begin";
    public static final String EndFlag = "map.end";

    public UIMapNode(String text) {
        super(text);
    }

    @Override
    public String getValue() {
        var params = this.getTemplate().getMap();
        if (params == null)
            return this.getSourceText();

        var sb = new StringBuffer();
        for (var key : params.keySet()) {
            var value = params.get(key);
            for (var item : this.getItems()) {
                if (item instanceof UIValueNode child) {
                    if ("map.key".equals(item.getText()))
                        sb.append(key);
                    else if ("map.value".equals(item.getText()))
                        sb.append(value);
                    else
                        sb.append(child.getSourceText());
                } else
                    sb.append(item.getSourceText());
            }
        }
        return sb.toString();
    }

    @Override
    protected String getEndFlag() {
        return EndFlag;
    }

}
