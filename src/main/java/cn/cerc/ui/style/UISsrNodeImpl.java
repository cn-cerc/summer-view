package cn.cerc.ui.style;

public interface UISsrNodeImpl {

    void setTemplate(UITemplateImpl template);

    /**
     * 
     * @return 原始字符串
     */
    String getSourceText();

    /**
     * 
     * @return 预处理过的字符串
     */
    String getText();

    /**
     * 
     * @return 返回处理过的字符串
     */
    String getValue();

}
