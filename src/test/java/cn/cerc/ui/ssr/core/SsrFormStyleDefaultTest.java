package cn.cerc.ui.ssr.core;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cn.cerc.db.core.DataRow;
import cn.cerc.ui.ssr.form.VuiForm;

public class SsrFormStyleDefaultTest {

    @Test
    public void test1() {
        VuiForm form = new VuiForm(null);
        var style = form.defaultStyle();
        form.addBlock(style.getString("查询条件", "SearchText_"));
        form.dataRow(DataRow.of("SearchText_", "小"));
        form.addColumn("查询条件");
        form.strict(false);
        assertEquals(
                """
                        <form method='post' action='' id='form1' role='search'><ul><li>
                        <label for="SearchText_"><em>查询条件</em></label>
                        <div> <input type="text" name="SearchText_" id="SearchText_" value="小" autocomplete="off" placeholder="请输入查询条件"/> <span role="suffix-icon"></span>
                        </div>
                        </li></ul></form>""",
                form.toString());
    }

    @Test
    public void test2() {
        VuiForm form = new VuiForm(null);
        var style = form.defaultStyle();
        form.addBlock(style.getString("查询条件", "SearchText_").dialog("showDateDialog"));
        form.dataRow(DataRow.of("SearchText_", "小"));
        form.addColumn("查询条件");
        form.strict(false);
        assertEquals(
                """
                        <form method='post' action='' id='form1' role='search'><ul><li>
                        <label for="SearchText_"><em>查询条件</em></label>
                        <div> <input type="text" name="SearchText_" id="SearchText_" value="小" autocomplete="off" placeholder="请点击获取查询条件"/> <span role="suffix-icon"><a href="javascript:showDateDialog('SearchText_')"><img src="null" /></a></span>
                        </div>
                        </li></ul></form>""",
                form.toString());
    }

    @Test
    public void test3() {
        VuiForm form = new VuiForm(null);
        var style = form.defaultStyle();
        form.addBlock(style.getBoolean("是否可为空", "null_able_"));
        form.dataRow(DataRow.of("null_able_", ""));
        form.addColumn("是否可为空");
        form.strict(false);
        assertEquals("""
                <form method='post' action='' id='form1' role='search'><ul><li>
                <div role="switch">
                <input autocomplete="off" name="null_able_" id="null_able_" type="checkbox" value="1"  />
                </div>
                <label for="null_able_"><em>是否可为空</em></label>
                </li></ul></form>""", form.toString());
    }

    @Test
    public void test4() {
        VuiForm form = new VuiForm(null);
        var style = form.defaultStyle();
        Map<String, String> statusMap = new LinkedHashMap<String, String>();
        statusMap.put("0", "全部");
        statusMap.put("1", "待审核");
        statusMap.put("2", "已审核");
        form.addBlock(style.getMap("单据状态", "status_")).setMap(statusMap);
        form.dataRow(DataRow.of("status_", "1"));
        form.addColumn("单据状态");
        form.strict(false);
        assertEquals(
                """
                        <form method='post' action='' id='form1' role='search'><ul><li>
                        <label for="status_"><em>单据状态</em></label>
                        <div>
                        <select id="status_" name="status_"> <option value="0" >全部</option> <option value="1" selected>待审核</option> <option value="2" >已审核</option> </select>
                        </div>
                        </li></ul></form>""",
                form.toString());
    }

    @Test
    public void test5() {
        VuiForm form = new VuiForm(null);
        var style = form.defaultStyle();
        form.addBlock(style.getCodeName("制单人员", "code_", "showCusCodeDialog"));
        form.dataRow(DataRow.of("code_", "CW001", "code__name", "张三"));
        form.addColumn("制单人员");
        form.strict(false);
        assertEquals(
                """
                        <form method='post' action='' id='form1' role='search'><ul><li>
                        <label for="code__name"><em>制单人员</em></label>
                        <div>
                        <input type="hidden" name="code_" id="code_" value="CW001">
                        <input type="text" name="code__name" id="code__name" value="张三" autocomplete="off" placeholder="请点击获取制单人员"readonly>
                        <span role="suffix-icon">
                        <a href="javascript:showCusCodeDialog('code_,code__name')">
                        <img src="null">
                        </a>
                        </span>
                        </div>
                        </li></ul></form>""",
                form.toString());
    }

    @Test
    public void test6() {
        VuiForm form = new VuiForm(null);
        var style = form.defaultStyle();
        form.addBlock(style.getDate("起始日期", "start_date_"));
        form.dataRow(DataRow.of("start_date_", "2022-11-02"));
        form.addColumn("起始日期");
        form.strict(false);
        assertEquals(
                """
                        <form method='post' action='' id='form1' role='search'><ul><li>
                        <label for="start_date_"><em>起始日期</em></label>
                        <div>
                        <input type="text" name="start_date_" id="start_date_" value="2022-11-02" autocomplete="off" placeholder="请点击获取起始日期"/>
                        <span role="suffix-icon"><a href="javascript:showDateDialog('start_date_')">
                        <img src="null" />
                        </a></span>
                        </div>
                        </li></ul></form>""",
                form.toString());
    }

    @Test
    public void test7() {
        VuiForm form = new VuiForm(null);
        var style = form.defaultStyle();
        form.addBlock(style.getDatetime("起始时间", "start_time_"));
        form.dataRow(DataRow.of("start_time_", "2022-11-02 11:23:21"));
        form.addColumn("起始时间");
        form.strict(false);
        assertEquals(
                """
                        <form method='post' action='' id='form1' role='search'><ul><li>
                        <label for="start_time_"><em>起始时间</em></label>
                        <div>
                        <input type="text" name="start_time_" id="start_time_" value="2022-11-02 11:23:21" autocomplete="off" placeholder="请点击获取起始时间"/>
                        <span role="suffix-icon"><a href="javascript:showDateTimeDialog('start_time_')">
                        <img src="null" />
                        </a></span>
                        </div>
                        </li></ul></form>""",
                form.toString());
    }

    @Test
    public void test8() {
        VuiForm form = new VuiForm(null);
        var style = form.defaultStyle();
        form.addBlock(style.getDateRange("单据日期", "start_date_", "end_date_"));
        form.dataRow(DataRow.of("start_date_", "2022-11-02", "end_date_", "2023-10-22"));
        form.addColumn("单据日期");
        form.strict(false);
        assertEquals(
                """
                        <form method='post' action='' id='form1' role='search'><ul><li>
                        <label for="start_date_"><em>单据日期</em></label>
                        <div class="dateArea">
                        <input autocomplete="off" name="start_date_" id="start_date_" type="text" class="dateAreaInput" value="2022-11-02"   />
                        <span>/</span>
                        <input autocomplete="off" name="end_date_" id="end_date_" type="text" class="dateAreaInput" value="2023-10-22"   />
                        <span role="suffix-icon">
                        <a href="javascript:showDateAreaDialog('start_date_', 'end_date_')">
                        <img src="null" />
                        </a>
                        </span>
                        </div>
                        </li></ul></form>""",
                form.toString());
    }

    @Test
    public void test9() {
        VuiForm form = new VuiForm(null);
        var style = form.defaultStyle();
        form.addBlock(style.getTextarea("详细地址", "address_"));
        form.dataRow(DataRow.of("address_", "宏宇商务大厦686"));
        form.addColumn("详细地址");
        form.strict(false);
        assertEquals("""
                <form method='post' action='' id='form1' role='search'><ul><li>
                <label for="address_"><em>详细地址</em></label>
                <div>
                <textarea name="address_" id="address_">宏宇商务大厦686</textarea>
                <span role="suffix-icon"></span>
                </div>
                </li></ul></form>""", form.toString());
    }

    @Test
    public void test10() {
        VuiForm form = new VuiForm(null);
        var style = form.defaultStyle();
        form.addBlock(style.getNumber("单据类型", "type_").toList(List.of("草稿", "生效", "作废")));
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("1", "草稿");
        map.put("2", "生效");
        map.put("3", "作废");
        form.addBlock(style.getString("运单类型", "type2_").toMap(map));
        form.dataRow(DataRow.of("type_", "1", "type2_", "3"));
        form.addColumn("单据类型");
        form.addColumn("运单类型");

        form.strict(false);
        assertEquals(
                """
                        <form method='post' action='' id='form1' role='search'><ul><li>
                        <label for="type_"><em>单据类型</em></label>
                        <div> <select id="type_" name="type_">  <option value="0" >草稿</option><option value="1" selected>生效</option><option value="2" >作废</option></select> <span role="suffix-icon"></span>
                        </div>
                        </li><li>
                        <label for="type2_"><em>运单类型</em></label>
                        <div> <select id="type2_" name="type2_"> <option value="1" >草稿</option><option value="2" >生效</option><option value="3" selected>作废</option></select> <span role="suffix-icon"></span>
                        </div>
                        </li></ul></form>""",
                form.toString());
    }

}
