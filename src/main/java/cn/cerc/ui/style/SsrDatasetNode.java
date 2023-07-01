package cn.cerc.ui.style;

public class SsrDatasetNode extends SsrForeachNode {
    public static final String StartFlag = "dataset.begin";
    public static final String EndFlag = "dataset.end";

    public SsrDatasetNode(String text) {
        super(text);
    }

    @Override
    public String getHtml() {
        var dataSet = this.getTemplate().getDataSet();
        if (dataSet == null)
            return this.getText();

        if (dataSet.size() == 0)
            return "";

        int save_recNo = dataSet.recNo();
        try {
            var sb = new StringBuffer();
            dataSet.first();
            while (dataSet.fetch()) {
                for (var item : this.getItems()) {
                    if (item instanceof SsrIfNode child) {
                        sb.append(child.getHtml());
                    } else if (item instanceof SsrValueNode child) {
                        if ("dataset.rec".equals(item.getField()))
                            sb.append(dataSet.recNo());
                        else
                            sb.append(child.getHtml());
                    } else
                        sb.append(item.getText());
                }
            }
            return sb.toString();
        } finally {
            dataSet.setRecNo(save_recNo);
        }
    }

    @Override
    protected String getEndFlag() {
        return EndFlag;
    }

}