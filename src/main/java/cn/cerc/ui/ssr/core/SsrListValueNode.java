package cn.cerc.ui.ssr.core;

public class SsrListValueNode extends SsrValueNode {
    private static final String Flag = "list.value";

    public SsrListValueNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrBlock block) {
        var list = block.getListProxy();
        if (list != null) {
            return list.value();
        } else {
            return this.getText();
        }
    }

    public static boolean is(String text) {
        return Flag.equals(text) || "list.item".equals(text);
    }
}
