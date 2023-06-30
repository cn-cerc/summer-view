package cn.cerc.ui.style;

public class UIListNode extends UIForeachNode {
    public static final String StartFlag = "list.begin";
    public static final String EndFlag = "list.end";

    public UIListNode(String text) {
        super(text);
    }

    @Override
    public String getValue() {
        var list = this.getTemplate().getList();
        if (list == null)
            return this.getSourceText();

        var sb = new StringBuffer();
        for (var param : list) {
            for (var item : this.getItems()) {
                if (item instanceof UIValueNode value) {
                    if ("list.item".equals(item.getField()))
                        sb.append(param);
                    else
                        sb.append(value.getSourceText());
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
