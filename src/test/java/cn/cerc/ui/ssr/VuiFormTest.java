package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.cerc.db.core.DataRow;
import cn.cerc.ui.ssr.core.PropertiesWriter;
import cn.cerc.ui.ssr.form.VuiForm;

public class VuiFormTest {

    @Test
    public void test_base() {
        var row = new DataRow();
        row.setValue("code_", "001").setValue("name_", "a01");
        var form = new VuiForm(null, "");
        form.dataRow(row);
        form.addColumn("name_", "code_");
        assertEquals(
                "<form method='post' action='' id='form1' role='search'><ul>name_: <input type=\"text\" name=\"name_\" value=\"a01\">code_: <input type=\"text\" name=\"code_\" value=\"001\"></ul></form>",
                form.toString());
    }

    @Test
    public void test_virtual() {
        var form = new VuiForm(null, "");
        form.setId("form1");
        form.action("FrmDept");
        var style = form.defaultStyle();
        form.addBlock(style.getString("部门代码", "code_"));
        form.addBlock(style.getString("部门名称", "name_"));

        var root = new ObjectMapper().createObjectNode();
        form.writeProperties(new PropertiesWriter(root));
        assertEquals(2, form.getComponentCount());
        assertEquals(
                """
                        {"class":"UISsrForm","id":"form1","container":true,"action":"","dataRow":"","components":[{"class":"FormStringField","id":"部门代码","title":"部门代码","field":"code_","mark":"","placeholder":"","dialog":"","patten":"","readonly":false,"required":false,"autofocus":false},{"class":"FormStringField","id":"部门名称","title":"部门名称","field":"name_","mark":"","placeholder":"","dialog":"","patten":"","readonly":false,"required":false,"autofocus":false}]}                """
                        .trim(),
                root.toString());
    }

}
