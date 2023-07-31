package cn.cerc.ui.ssr;

public class SsrMapIndexNode extends SsrValueNode {
    private static final String Flag = "map.index";

    public SsrMapIndexNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrBlockImpl block) {
        var map = block.getMap();
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
