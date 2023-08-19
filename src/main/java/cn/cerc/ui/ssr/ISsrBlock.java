package cn.cerc.ui.ssr;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.ui.ssr.editor.ISsrBoard;

public interface ISsrBlock extends ISsrOption {
    public static final String Fields = "fields";

    SsrNodes nodes();

    ISsrBlock setNodes(SsrNodes style);

    /**
     * 
     * @param value 添加到list对象
     * @return 返回对象本身
     */
    ISsrBlock toList(String... value);

    default ISsrBlock toList(List<String> list) {
        toList(list.toArray(new String[list.size()]));
        return this;
    }

    /**
     * 
     * 
     * @return 返回 list 数据源
     */
    List<String> getList();

    ISsrBlock toMap(String Key, String value);

    default ISsrBlock toMap(Map<String, String> map) {
        for (var key : map.keySet())
            toMap(key, map.get(key));
        return this;
    }

    default ISsrBlock toMap(Enum<?>[] enums) {
        for (var item : enums)
            toMap("" + item.ordinal(), item.name());
        return this;
    }

    /**
     * 请改使用 toMap
     * 
     * @param map 设置 map 数据源
     * @return 返回对象本身
     */
    ISsrBlock setMap(Map<String, String> map);

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
    ISsrBlock setDataRow(DataRow dataRow);

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
    ISsrBlock setDataSet(DataSet dataSet);

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
    ISsrBlock onCallback(String nodeId, Supplier<String> supplier);

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

    ISsrBlock id(String id);

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

    ISsrBlock onGetValue(OnGetValueEvent onGetValue);

    /**
     * 固定当前查询字段（不会出现在配置列表中）
     * 
     * @param form
     * @return
     */
    ISsrBlock fixed(ISsrBoard form);

    /**
     * 
     * @param fields 设置字段列表，以逗号隔开
     * @return 返回自身
     */
    default ISsrBlock fields(String... fields) {
        this.option(Fields, String.join(",", fields));
        return this;
    }

    ISsrBlock setTemplate(SsrTemplate template);

}
