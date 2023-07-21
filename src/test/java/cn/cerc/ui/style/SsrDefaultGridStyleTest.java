package cn.cerc.ui.style;

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
        grid.setDataSet(ds);

        var map = new LinkedHashMap<String, String>();
        map.put("a", "张三");
        map.put("b", "李四");
        map.put("c", "王五");

        var style = new SsrDefaultGridStyle();
        grid.addField(style.getIt(2));
        grid.addField(style.getString("Name_", "部门名称", 10));
        grid.addField(style.getBoolean("Final_", "状态", 4, "已审核"));
        grid.addField(style.getOption("Type_", "类别", 10, map));
        grid.addField(style.getOpera("Code_", 4, "FrmView?mode=3"));

        assertEquals(
                "<table><tr><th width=2>序</th><th width=10>部门名称</th><th width=4>已审核</th><th width=10>类别</th><th width=4>操作</th></tr><tr><td>1</td><td>研发部</td><td><input type='checkbox' value=''>状态</input></td><select><option value =\"a\" selected>张三</option><option value =\"b\" >李四</option><option value =\"c\" >王五</option></select><td><a href='FrmView?mode=3&Code_=001'>内容</a></td></tr><tr><td>2</td><td>生产部</td><td><input type='checkbox' value='true'>状态</input></td><select><option value =\"a\" >张三</option><option value =\"b\" >李四</option><option value =\"c\" >王五</option></select><td><a href='FrmView?mode=3&Code_=002'>内容</a></td></tr></table>",
                grid.toString());
    }

}
