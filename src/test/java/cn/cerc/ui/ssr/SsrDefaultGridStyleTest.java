package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;

import org.junit.Test;

import cn.cerc.db.core.DataSet;

public class SsrDefaultGridStyleTest {

    @Test
    public void test() {
        var ds = new DataSet();
        ds.append().setValue("Name_", "研发部").setValue("Code_", "001").setValue("Type_", "a");
        ds.append().setValue("Name_", "生产部").setValue("Code_", "002").setValue("Final_", true);

        var grid = new UISsrGrid(null, "");
        grid.dataSet(ds);

        var map = new LinkedHashMap<String, String>();
        map.put("a", "张三");
        map.put("b", "李四");
        map.put("c", "王五");

        var style = grid.defaultStyle();
        grid.addBlock(style.getIt("序", 2));
        grid.addBlock(style.getString("部门名称", "Name_", 10));
        grid.addBlock(style.getCheckBox("状态", "Final_", 4));
        grid.addBlock(style.getMap("类别", "Type_", 10, map));
        grid.addBlock(style.getOpera(4)).onCallback("url", () -> {
            return "FrmView?code=" + ds.getString("Code_");
        });

        grid.addColumn("序", "部门名称", "状态", "类别", "操作");
        assertEquals(
                """
                        <div id='grid' class='scrollArea'><table class='dbgrid'><tr>
                        <th style='width: 2em'>序</th><th style='width: 10em'>部门名称</th><th style='width: 4em'>状态</th><th style='width: 10em'>类别</th><th style='width: 4em'>操作</th></tr>
                        <tr>
                        <td align='center' role='_it_'>1</td><td align='left' role='Name_'>研发部</td><td align='center' role='Final_'>
                        <span><input type='checkbox' name='checkBoxName' value='1' /></span>
                        </td><td role='Type_'>张三</td><td align='center' role='_opera_'><a href='FrmView?code=001'>内容</a></td></tr>
                        <tr>
                        <td align='center' role='_it_'>2</td><td align='left' role='Name_'>生产部</td><td align='center' role='Final_'>
                        <span><input type='checkbox' name='checkBoxName' value='1' checked /></span>
                        </td><td role='Type_'></td><td align='center' role='_opera_'><a href='FrmView?code=002'>内容</a></td></tr>
                        </table></div>""",
                grid.toString());
    }

    @Test
    public void test1() {
        var ds = new DataSet();
        ds.append().setValue("Name_", "货车").setValue("Code_", "001").setValue("Type_", "0");
        ds.append().setValue("Name_", "汽车").setValue("Code_", "002").setValue("Type_", "1");

        var grid = new UISsrGrid(null, "");
        grid.dataSet(ds);

        var map = new LinkedHashMap<String, String>();
        map.put("0", "自有");
        map.put("1", "租赁");

        var style = grid.defaultStyle();
        grid.addBlock(style.getIt());
        grid.addBlock(style.getString("车辆名称", "Name_", 10, "right"));
        grid.addBlock(style.getMap("类别", "Type_", 10, map));
        grid.addBlock(style.getOpera(4)).onCallback("url", () -> {
            return "FrmView?code=" + ds.getString("Code_");
        });

        grid.addColumn("序", "车辆名称", "类别", "操作");
        assertEquals(
                """
                        <div id='grid' class='scrollArea'><table class='dbgrid'><tr>
                        <th style='width: 2em'>序</th><th style='width: 10em'>车辆名称</th><th style='width: 10em'>类别</th><th style='width: 4em'>操作</th></tr>
                        <tr>
                        <td align='center' role='_it_'>1</td><td align='right' role='Name_'>货车</td><td role='Type_'>自有</td><td align='center' role='_opera_'><a href='FrmView?code=001'>内容</a></td></tr>
                        <tr>
                        <td align='center' role='_it_'>2</td><td align='right' role='Name_'>汽车</td><td role='Type_'>租赁</td><td align='center' role='_opera_'><a href='FrmView?code=002'>内容</a></td></tr>
                        </table></div>""",
                grid.toString());
    }

}
