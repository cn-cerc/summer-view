package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.ssr.grid.VuiGrid;

public class SsrGridStyleDefaultTest {

    public void test1() {
        DataSet ds = new DataSet();
        ds.append().setValue("name_", "张三");
        ds.append().setValue("name_", "李四");
        ds.append().setValue("name_", "王五");
        VuiGrid grid = new VuiGrid(null);
        grid.dataSet(ds);
        var style = grid.defaultStyle();
        grid.addBlock(style.getIt());
        grid.addColumn("序");
        assertEquals("""
                <div id='grid' class='scrollArea'><table class='dbgrid'><tr>
                <th style='width: 2em'>序</th></tr>
                <tr>
                <td align='center'>1</td></tr>
                <tr>
                <td align='center'>2</td></tr>
                <tr>
                <td align='center'>3</td></tr>
                </table></div>""", grid.toString());
    }

}
