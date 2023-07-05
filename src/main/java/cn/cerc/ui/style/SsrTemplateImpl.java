package cn.cerc.ui.style;

import java.util.List;
import java.util.Map;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;

public interface SsrTemplateImpl {

    /**
     * 设置模版文本
     * 
     * @param templateText
     * @return 返回对象本身
     */
    SsrTemplateImpl setTemplateText(String templateText);

    /**
     * 
     * @param list 设置 list 数据源
     * @return 返回对象本身
     */
    SsrTemplateImpl setList(List<String> list);

    /**
     * @return 返回 list 数据源
     */
    List<String> getList();


    /**
     * 
     * @param map 设置 map 数据源
     * @return 返回对象本身
     */
    SsrTemplateImpl setMap(Map<String, String> map);

    /**
     * 
     * @return 返回 map 数据源
     */
    Map<String, String> getMap();

    /**
     * 
     * @param dataRow 设置 dataRow 数据源
     * @return 返回对象本身
     */
    SsrTemplateImpl setDataRow(DataRow dataRow);

    /**
     * 
     * @return 返回 dataRow 数据源
     */
    DataRow getDataRow();

    /**
     * 
     * @param dataSet 设置 dataSet 数据源
     * @return 返回对象本身
     */
    SsrTemplateImpl setDataSet(DataSet dataSet);

    /**
     * 
     * @return 返回 dataSet 数据源
     */
    DataSet getDataSet();

    /**
     * 
     * @param callback 赋值回调函数
     * @return 设置回调函数
     */
    SsrTemplateImpl setCallback(SsrCallbackImpl callback);

    /**
     * 
     * @return 返回回调函数
     */
    SsrCallbackImpl getCallback();

    /**
     * 
     * @param strict 是否执行严格模式，默认为 true
     * @return 返回对象本身
     */
    SsrTemplateImpl setStrict(boolean strict);

    /**
     * 
     * @return 返回解析模式，默认为严格模式
     */
    boolean isStrict();

    /**
     * 
     * @return 返回模版的解析结果
     */
    String getHtml();

}
