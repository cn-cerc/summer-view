package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataRow;

public class UISsrFormTest {

    @Test
    public void test_base() {
        var row = new DataRow();
        row.setValue("code_", "001").setValue("name_", "a01");
        var grid = new UISsrForm(null, "");
        grid.setDataRow(row);
        grid.addField("name_", "code_");
        assertEquals(
                "<form>name_: <input type=\"text\" name=\"name_\" value=\"a01\">code_: <input type=\"text\" name=\"code_\" value=\"001\"></form>",
                grid.toString());
    }

}
