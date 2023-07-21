package cn.cerc.ui.style;

public class SsrMapValueNode extends SsrValueNode {
    public static final String Flag = "map.value";

    public SsrMapValueNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(SsrTemplateImpl template) {
        var map = template.getMap();
        if (map != null) {
            return template.getMapProxy().value();
        } else {
            return this.getText();
        }
    }

    public static boolean is(String text) {
        return Flag.equals(text);
    }
}
