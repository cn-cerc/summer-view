package cn.cerc.ui.style;

import java.util.List;
import java.util.Map;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.ui.style.SsrTemplate.ForeachList;
import cn.cerc.ui.style.SsrTemplate.ForeachMap;

public interface SsrTemplateImpl {

    /**
     * 
     * @param value 添加到list对象
     * @return 返回对象本身
     */
    SsrTemplateImpl toList(String... value);

    /**
     * 
     * 
     * @return 返回 list 数据源
     */
    List<String> getList();

    SsrTemplateImpl toMap(String Key, String value);

    /**
     * 请改使用 toMap
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

    String templateText();

    SsrTemplateImpl setId(String id);

    String id();

    ForeachMap getForeachMap();

    ForeachList getForeachList();
}
