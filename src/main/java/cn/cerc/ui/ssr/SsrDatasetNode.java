package cn.cerc.ui.ssr;

public class SsrDatasetNode extends SsrContainerNode {
    public static final SsrContainerSignRecord Sign = new SsrContainerSignRecord("dataset.begin", "dataset.end",
            (text) -> new SsrDatasetNode(text));

    public SsrDatasetNode(String text) {
        super(text);
    }

    @Override
    public String getHtml(ISsrBlock block) {
        var dataSet = block.getDataSet();
        if (dataSet == null)
            return this.getText();

        if (dataSet.size() == 0)
            return "";

        int save_recNo = dataSet.recNo();
        try {
            var sb = new StringBuffer();
            dataSet.first();
            while (dataSet.fetch()) {
                for (var item : this.getItems())
                    sb.append(item.getHtml(block));
            }
            return sb.toString();
        } finally {
            dataSet.setRecNo(save_recNo);
        }
    }

    @Override
    protected String getEndFlag() {
        return Sign.endFlag();
    }

    public static boolean is(String text) {
        return text.equals(Sign.beginFlag()) || text.equals(Sign.endFlag());
    }

}
