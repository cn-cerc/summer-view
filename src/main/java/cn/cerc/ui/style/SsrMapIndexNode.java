package cn.cerc.ui.style;

public class SsrMapIndexNode extends SsrValueNode {
    private static final String Flag = "map.index";

    public SsrMapIndexNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrTemplateImpl template) {
        var map = template.getMap();
        if (map != null) {
            return template.getMapProxy().index();
        } else {
            return this.getText();
        }
    }

    public static boolean is(String text) {
        return Flag.equals(text);
    }
}
