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
    String getField();

    /**
     * 
     * @return 若为特殊字段，则返回字段名
     */
    String getValue();

}
