package cn.cerc.ui.ssr.core;

public class SsrMapKeyNode extends SsrValueNode {
    public static final String Flag = "map.key";

    public SsrMapKeyNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrBlock block) {
        var map = block.map();
        if (map != null) {
            return block.getMapProxy().key();
        } else {
            return this.getText();
        }
    }

    public static boolean is(String text) {
        return Flag.equals(text);
    }
}
