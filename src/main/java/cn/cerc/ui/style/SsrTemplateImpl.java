package cn.cerc.ui.style;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.ui.style.SsrTemplate.ListProxy;
import cn.cerc.ui.style.SsrTemplate.MapProxy;

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
     * @param nodeId   节点id
     * @param supplier 赋值回调函数
     * @return 设置回调函数
     */
    SsrTemplateImpl onCallback(String nodeId, Supplier<String> supplier);

    /**
     * 
     * @return 返回回调函数
     */
    Map<String, Supplier<String>> getCallback();

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

    MapProxy getMapProxy();

    ListProxy getListProxy();

    /**
     * 
     * @return 请改使用 setOption 与 getOption
     */
    @Deprecated
    Map<String, String> getOptions();

    SsrTemplateImpl setOption(String key, String value);

    Optional<String> getOption(String key);

    Optional<String> getValue(String field);
}
