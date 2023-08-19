package cn.cerc.ui.ssr.core;

public class SsrMapValueNode extends SsrValueNode {
    private static final String Flag = "map.value";

    public SsrMapValueNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrBlock block) {
        var map = block.map();
        if (map != null) {
            return block.getMapProxy().value();
        } else {
            return this.getText();
        }
    }

    public static boolean is(String text) {
        return Flag.equals(text);
    }
}
