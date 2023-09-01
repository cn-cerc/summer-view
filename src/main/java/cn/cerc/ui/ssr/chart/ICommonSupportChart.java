package cn.cerc.ui.ssr.chart;

public interface ICommonSupportChart extends ISupportChart {

    public String fields();

    public ICommonSupportChart field(String field);

    public String title();

    public ICommonSupportChart title(String title);
}
