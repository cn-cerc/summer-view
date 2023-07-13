package cn.cerc.ui.style;

public class SsrDataSetRecNode extends SsrValueNode {
    public static final String FirstFlag = "dataset.rec";

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
        return text.equals(FirstFlag);
    }

}
