package cn.cerc.ui.ssr;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;

public interface SsrBlockImpl extends SsrOptionImpl {

    SsrNodes nodes();

    SsrBlockImpl setNodes(SsrNodes style);

    /**
     * 
     * @param value 添加到list对象
     * @return 返回对象本身
     */
    SsrBlockImpl toList(String... value);

    /**
     * 
     * 
     * @return 返回 list 数据源
     */
    List<String> getList();

    SsrBlockImpl toMap(String Key, String value);

    /**
     * 请改使用 toMap
     * 
     * @param map 设置 map 数据源
     * @return 返回对象本身
     */
    SsrBlockImpl setMap(Map<String, String> map);

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
    SsrBlockImpl setDataRow(DataRow dataRow);

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
    SsrBlockImpl setDataSet(DataSet dataSet);

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
    SsrBlockImpl onCallback(String nodeId, Supplier<String> supplier);

    /**
     * 
     * @return 返回回调函数
     */
    Map<String, Supplier<String>> getCallback();

    /**
     * 
     * @return 返回模版的解析结果
     */
    String getHtml();

    String templateText();

    SsrBlockImpl setId(String id);

    String id();

    SsrMapProxy getMapProxy();

    SsrListProxy getListProxy();

    /**
     * 
     * @return 请改使用 setOption 与 getOption
     */
    @Deprecated
    Map<String, String> getOptions();

    Optional<String> getValue(String field);

    SsrBlockImpl onGetValue(OnGetValueEvent onGetValue);

    /**
     * 固定当前查询字段（不会出现在配置列表中）
     * 
     * @param form
     * @return
     */
    SsrBlockImpl fixed(SsrComponentImpl form);

}
