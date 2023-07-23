package cn.cerc.ui.grid;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.editor.EditorFactory;

public class UIGridViewTest {

    @Test
    public void test() {
        DataSet ds = new DataSet();
        ds.append();
        ds.setValue("code", "a01");
        ds.setValue("name", "jason");
        ds.setValue("sex", true);
        ds.append();
        ds.setValue("code", "a02");
        ds.setValue("name", "bade");
        ds.setValue("sex", false);

        ds.fields().get("code").setName("工号");
        ds.fields().get("name").setName("姓名");
        ds.fields().get("sex").setName("性别").onGetSetText(EditorFactory.ofBoolean("女的", "男的"));

        UIGridView grid = new UIGridView(null).setDataSet(ds);
        grid.addField("Name_").setAlignRight();
        grid.setDataStyle(new UIDataStyle());
        grid.setActive(true);
//        grid.addField("sex"); //指定栏位输出
        assertEquals(
                """
                        <table class='dbgrid gridView'><tr><th onclick='gridViewSort(this)'>序</th><th>Name_</th></tr><tr><td align='center'>1</td><td></td></tr>
                        <tr><td align='center'>2</td><td></td></tr>
                        </table>
                        """,
                grid.toString());
    }

}
