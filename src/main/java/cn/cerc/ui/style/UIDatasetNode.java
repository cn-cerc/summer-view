package cn.cerc.ui.style;

public class UIDatasetNode extends UIForeachNode {
    public static final String StartFlag = "dataset.begin";
    public static final String EndFlag = "dataset.end";

    public UIDatasetNode(String text) {
        super(text);
    }

    @Override
    public String getValue() {
        var dataSet = this.getTemplate().getDataSet();
        if (dataSet == null)
            return this.getSourceText();

        if (dataSet.size() == 0)
            return "";

        int save_recNo = dataSet.recNo();
        try {
            var sb = new StringBuffer();
            dataSet.first();
            while (dataSet.fetch()) {
                for (var item : this.getItems()) {
                    if (item instanceof UIIfNode child) {
                        sb.append(child.getValue());
                    } else if (item instanceof UIValueNode child) {
                        if ("dataset.rec".equals(item.getField()))
                            sb.append(dataSet.recNo());
                        else
                            sb.append(child.getValue());
                    } else
                        sb.append(item.getSourceText());
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
