package cn.cerc.ui.style;

public class SsrListValueNode extends SsrValueNode {
    private static final String Flag = "list.value";

    public SsrListValueNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrTemplateImpl template) {
        var list = template.getListProxy();
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
