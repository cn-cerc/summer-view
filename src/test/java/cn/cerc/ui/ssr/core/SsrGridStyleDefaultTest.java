package cn.cerc.ui.ssr.core;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.ssr.grid.GridGroup;
import cn.cerc.ui.ssr.grid.VuiGrid;

public class SsrGridStyleDefaultTest {

    @Test
    public void test() {
        var ds = new DataSet();
        ds.append().setValue("Name_", "研发部").setValue("Code_", "001").setValue("Type_", "a");
        ds.append().setValue("Name_", "生产部").setValue("Code_", "002").setValue("Final_", true);

        var grid = new VuiGrid(null, "");
        grid.dataSet(ds);

        var map = new LinkedHashMap<String, String>();
        map.put("a", "张三");
        map.put("b", "李四");
        map.put("c", "王五");

        var style = grid.defaultStyle();
        grid.addBlock(style.getIt("序", 2));
        grid.addBlock(style.getString("部门名称", "Name_", 10));
        grid.addBlock(style.getBoolean("状态", "Final_", 4).readonly(false));
        grid.addBlock(style.getString("类别", "Type_", 10).toMap(map));
        grid.addBlock(style.getOpera(4)).onCallback("url", () -> {
            return "FrmView?code=" + ds.getString("Code_");
        });

        grid.addColumn("序", "部门名称", "状态", "类别", "操作");
        assertEquals(
                """
                        <div id='grid' class='scrollArea'><table class='dbgrid'><tr>
                        <th style='width: 2em' onclick="gridSort(this,'_it_')">序</th><th style='width: 10em' onclick="gridSort(this,'Name_')">部门名称</th><th style='width: 4em' onclick="gridSort(this,'Final_')">状态</th><th style='width: 10em' onclick="gridSort(this,'Type_')">类别</th><th style='width: 4em' onclick="gridSort(this,'_opera_')">操作</th></tr>
                        <tr>
                        <td align='center' role='_it_'>1</td><td align='left' role='Name_'>研发部</td><td align='center' role='Final_'>
                        <span><input type='checkbox' name='Final_' value='1' /></span>
                        </td><td align='left' role='Type_'>张三</td><td align='center' role='_opera_'><a href='FrmView?code=001'>内容</a></td></tr>
                        <tr>
                        <td align='center' role='_it_'>2</td><td align='left' role='Name_'>生产部</td><td align='center' role='Final_'>
                        <span><input type='checkbox' name='Final_' value='1' checked /></span>
                        </td><td align='left' role='Type_'></td><td align='center' role='_opera_'><a href='FrmView?code=002'>内容</a></td></tr>
                        </table></div>""",
                grid.toString());
    }

    @Test
    public void test1() {
        var ds = new DataSet();
        ds.append().setValue("Name_", "货车").setValue("Code_", "001").setValue("Type_", "0");
        ds.append().setValue("Name_", "汽车").setValue("Code_", "002").setValue("Type_", "1");

        var grid = new VuiGrid(null, "");
        grid.dataSet(ds);

        var style = grid.defaultStyle();
        grid.addBlock(style.getIt());
        grid.addBlock(style.getString("车辆名称", "Name_", 10, "right"));
        grid.addBlock(style.getNumber("类别", "Type_", 10).toList(List.of("自有", "租赁")));
        grid.addBlock(style.getOpera(4)).onCallback("url", () -> {
            return "FrmView?code=" + ds.getString("Code_");
        });

        grid.addColumn("序", "车辆名称", "类别", "操作");
        assertEquals(
                """
                        <div id='grid' class='scrollArea'><table class='dbgrid'><tr>
                        <th style='width: 2em' onclick="gridSort(this,'_it_')">序</th><th style='width: 10em' onclick="gridSort(this,'Name_')">车辆名称</th><th style='width: 10em' onclick="gridSort(this,'Type_')">类别</th><th style='width: 4em' onclick="gridSort(this,'_opera_')">操作</th></tr>
                        <tr>
                        <td align='center' role='_it_'>1</td><td align='right' role='Name_'>货车</td><td align='' role='Type_'>自有</td><td align='center' role='_opera_'><a href='FrmView?code=001'>内容</a></td></tr>
                        <tr>
                        <td align='center' role='_it_'>2</td><td align='right' role='Name_'>汽车</td><td align='' role='Type_'>租赁</td><td align='center' role='_opera_'><a href='FrmView?code=002'>内容</a></td></tr>
                        </table></div>""",
                grid.toString());
    }

    @Test
    public void test2() {
        var ds = new DataSet();
        ds.append().setValue("Name_", "研发部").setValue("Code_", "001").setValue("Type_", "a");
        ds.append().setValue("Name_", "生产部").setValue("Code_", "002").setValue("Final_", true);

        var grid = new VuiGrid(null, "");
        grid.dataSet(ds);

        var map = new LinkedHashMap<String, String>();
        map.put("a", "张三");
        map.put("b", "李四");
        map.put("c", "王五");

        var style = grid.defaultStyle();
        grid.addBlock(style.getIt("序", 2));
        grid.addBlock(style.getString("部门名称", "Name_", 10));
        grid.addBlock(style.getBoolean("状态", "Final_", 4).readonly(false));
        grid.addBlock(style.getString("类别", "Type_", 10).toMap(map));
        grid.addBlock(style.getOpera(4)).onCallback("url", () -> {
            return "FrmView?code=" + ds.getString("Code_");
        });

        grid.addColumn("序", "部门名称", "状态", "类别", "操作");
        assertEquals(
                """
                        <div id='grid' class='scrollArea'><table class='dbgrid'><tr>
                        <th style='width: 2em' onclick="gridSort(this,'_it_')">序</th><th style='width: 10em' onclick="gridSort(this,'Name_')">部门名称</th><th style='width: 4em' onclick="gridSort(this,'Final_')">状态</th><th style='width: 10em' onclick="gridSort(this,'Type_')">类别</th><th style='width: 4em' onclick="gridSort(this,'_opera_')">操作</th></tr>
                        <tr>
                        <td align='center' role='_it_'>1</td><td align='left' role='Name_'>研发部</td><td align='center' role='Final_'>
                        <span><input type='checkbox' name='Final_' value='1' /></span>
                        </td><td align='left' role='Type_'>张三</td><td align='center' role='_opera_'><a href='FrmView?code=001'>内容</a></td></tr>
                        <tr>
                        <td align='center' role='_it_'>2</td><td align='left' role='Name_'>生产部</td><td align='center' role='Final_'>
                        <span><input type='checkbox' name='Final_' value='1' checked /></span>
                        </td><td align='left' role='Type_'></td><td align='center' role='_opera_'><a href='FrmView?code=002'>内容</a></td></tr>
                        </table></div>""",
                grid.toString());
    }

    @Test
    public void test3() {
        var ds = new DataSet();
        ds.append().setValue("Name_", "张三").setValue("Sex_", "1");
        ds.append().setValue("Name_", "李四").setValue("Sex_", "");

        var grid = new VuiGrid(null, "");
        grid.dataSet(ds);

        var style = grid.defaultStyle();
        grid.addBlock(style.getString("姓名", "Name_", 10));
        grid.addBlock(style.getBoolean("性别", "Sex_", 4).trueText("男").falseText("女"));

        grid.addColumn("姓名", "性别");
        assertEquals(
                """
                        <div id='grid' class='scrollArea'><table class='dbgrid'><tr>
                        <th style='width: 10em' onclick="gridSort(this,'Name_')">姓名</th><th style='width: 4em' onclick="gridSort(this,'Sex_')">性别</th></tr>
                        <tr>
                        <td align='left' role='Name_'>张三</td><td align='center' role='Sex_'>
                        <span>男</span>
                        </td></tr>
                        <tr>
                        <td align='left' role='Name_'>李四</td><td align='center' role='Sex_'>
                        <span>女</span>
                        </td></tr>
                        </table></div>""",
                grid.toString());
    }

    @Test
    public void test4() {
        var ds = new DataSet();
        ds.append().setValue("Name_", "张三").setValue("Sex_", "1");
        ds.append().setValue("Name_", "李四").setValue("Sex_", "");

        var grid = new VuiGrid(null, "");
        grid.dataSet(ds);

        var style = grid.defaultStyle();
        GridGroup group = style.getGroup("姓名-性别", "group1", 10);
        grid.addBlock(group);
        group.addBlock(style.getString("姓名", "Name_", 10));
        group.addBlock(style.getBoolean("性别", "Sex_", 4).trueText("男").falseText("女"));
        group.addBlock(style.getOpera(4)).onCallback("url", () -> {
            return "www.baidu.com";
        });

        grid.addColumn("姓名-性别");
        assertEquals("""
                <div id='grid' class='scrollArea'><table class='dbgrid'><tr>
                <th style='width: 10em'>姓名-性别</th></tr>
                <tr>
                <td align='' role='group1'><span>张三</span><br/><span>
                <span>男</span>
                </span><br/><span><a href='www.baidu.com'>内容</a></span><br/></td></tr>
                <tr>
                <td align='' role='group1'><span>李四</span><br/><span>
                <span>女</span>
                </span><br/><span><a href='www.baidu.com'>内容</a></span><br/></td></tr>
                </table></div>""", grid.toString());
    }

    @Test
    public void test5() {
        var ds = new DataSet();
        ds.append().setValue("Num_", "10.7846574").setValue("total_", "10.7846574");
        ds.append().setValue("Num_", "124.4656475").setValue("total_", "124.4656475");

        var grid = new VuiGrid(null, "");
        grid.dataSet(ds);
        var style = grid.defaultStyle();
        grid.addBlock(style.getString("数量", "Num_", 10));
        grid.addBlock(style.getNumber("合计", "total_", 10));
        grid.addColumn("数量", "合计");
        assertEquals(
                """
                        <div id='grid' class='scrollArea'><table class='dbgrid'><tr>
                        <th style='width: 10em' onclick="gridSort(this,'Num_')">数量</th><th style='width: 10em' onclick="gridSort(this,'total_')">合计</th></tr>
                        <tr>
                        <td align='left' role='Num_'>10.7846574</td><td align='' role='total_'>10.7847</td></tr>
                        <tr>
                        <td align='left' role='Num_'>124.4656475</td><td align='' role='total_'>124.4656</td></tr>
                        </table></div>""",
                grid.toString());
    }
    
    @Test
    public void test6() {
        var ds = new DataSet();
        ds.append().setValue("Name_", "张三").setValue("total_", "10");
        ds.append().setValue("Name_", "李四").setValue("total_", "124");
        var grid = new VuiGrid(null, "");
        grid.dataSet(ds);
        var style = grid.defaultStyle();
        grid.addBlock(style.getString("姓名", "Name_", 10));
        grid.addBlock(style.getHidden("余额", "total_"));
        grid.addColumn("姓名");
        assertEquals(
                """
                        <div id='grid' class='scrollArea'><table class='dbgrid'><tr>
                        <th style='display: none;'
                        onclick="gridSort(this,'total_')">余额</th><th style='width: 10em' onclick="gridSort(this,'Name_')">姓名</th></tr>
                        <tr>
                        <td align='left' style="display: none;" role='total_'> <input type="text" name="total_" id="1_0" value="10"/> </td><td align='left' role='Name_'>张三</td></tr>
                        <tr>
                        <td align='left' style="display: none;" role='total_'> <input type="text" name="total_" id="2_0" value="124"/> </td><td align='left' role='Name_'>李四</td></tr>
                        </table></div>""",grid.toString());
    }
    
    @Test
    public void test7() {
        var ds = new DataSet();
        ds.append().setValue("Name_", "张三").setValue("total_", "10");
        ds.append().setValue("Name_", "李四").setValue("total_", "124");
        var grid = new VuiGrid(null, "");
        grid.dataSet(ds);
        var style = grid.defaultStyle();
        grid.addBlock(style.getString("姓名", "Name_", 10));
        grid.addBlock(style.getHidden("余额", "total_").readonly());
        grid.addColumn("姓名");
        assertEquals(
                """
                        <div id='grid' class='scrollArea'><table class='dbgrid'><tr>
                        <th style='display: none;'
                        onclick="gridSort(this,'total_')">余额</th><th style='width: 10em' onclick="gridSort(this,'Name_')">姓名</th></tr>
                        <tr>
                        <td align='left' style="display: none;" role='total_'> 10 </td><td align='left' role='Name_'>张三</td></tr>
                        <tr>
                        <td align='left' style="display: none;" role='total_'> 124 </td><td align='left' role='Name_'>李四</td></tr>
                        </table></div>""",grid.toString());
    }

}
