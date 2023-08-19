package cn.cerc.ui.ssr.core;

public class SsrMapIndexNode extends SsrValueNode {
    private static final String Flag = "map.index";

    public SsrMapIndexNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrBlock block) {
        var map = block.map();
        if (map != null) {
            return block.getMapProxy().index();
        } else {
            return this.getText();
        }
    }

    public static boolean is(String text) {
        return Flag.equals(text);
    }
}
