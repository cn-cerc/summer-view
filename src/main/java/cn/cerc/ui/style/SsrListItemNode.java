package cn.cerc.ui.style;

public class SsrListItemNode extends SsrValueNode {
    public static final String Flag = "list.item";

    public SsrListItemNode(String text) {
        super(text);
    }

    @Override
    public String getHtml() {
        var list = this.getTemplate().getForeachList();
        if (list != null) {
            return list.item();
        } else {
            return this.getText();
        }
    }

    public static boolean is(String text) {
        return Flag.equals(text);
    }
}
