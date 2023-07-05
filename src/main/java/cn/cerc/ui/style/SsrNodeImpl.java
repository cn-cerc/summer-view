package cn.cerc.ui.style;

public interface SsrNodeImpl {

    void setTemplate(SsrTemplateImpl template);

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
     * @return 若为特殊字段，则返回字段名
     */
    String getHtml();

}
