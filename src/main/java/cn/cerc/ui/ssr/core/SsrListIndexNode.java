package cn.cerc.ui.ssr.core;

public class SsrListIndexNode extends SsrValueNode {
    private static final String Flag = "list.index";

    public SsrListIndexNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrBlock block) {
        var list = block.getListProxy();
        if (list != null) {
            return list.index();
        } else {
            return this.getText();
        }
    }

    public static boolean is(String text) {
        return Flag.equals(text);
    }
}
