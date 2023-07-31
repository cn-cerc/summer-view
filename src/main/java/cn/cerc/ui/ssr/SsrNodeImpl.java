package cn.cerc.ui.ssr;

public interface SsrNodeImpl {
    /**
     * 
     * @return 原始字符串
     */
    String getText();

    /**
     * 
     * @return 预处理过的字符串
     */
    String getField();

    /**
     * 
     * @return 取得经过转换后的字符串
     */
    String getHtml(SsrBlockImpl block);

}
