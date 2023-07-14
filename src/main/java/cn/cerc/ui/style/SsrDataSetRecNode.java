package cn.cerc.ui.style;

public class SsrDataSetRecNode extends SsrValueNode {
    private static final String Flag = "dataset.rec";

    public SsrDataSetRecNode(String text) {
        super(text);
    }

    @Override
    public String getHtml() {
        var dataSet = this.getTemplate().getDataSet();
        if (dataSet != null) {
            return "" + dataSet.recNo();
        } else {
            return this.getText();
        }
    }

    public static boolean is(String text) {
        return text.equals(Flag);
    }

}
