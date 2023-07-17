package cn.cerc.ui.style;

public class SsrMapKeyNode extends SsrValueNode {
    public static final String Flag = "map.key";

    public SsrMapKeyNode(String text) {
        super(text);
    }

    @Override
    public String getHtml() {
        var map = this.getTemplate().getMap();
        if (map != null) {
            return this.getTemplate().getForeachMap().key();
        } else {
            return this.getText();
        }
    }

    public static boolean is(String text) {
        return Flag.equals(text);
    }
}