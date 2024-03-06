package cn.cerc.ui.ssr.form;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.cerc.db.core.DataRow;
import cn.cerc.ui.ssr.core.PropertiesWriter;

public class VuiFormTest {

    @Test
    public void test_base() {
        var row = new DataRow();
        row.setValue("code_", "001").setValue("name_", "a01");
        var form = new VuiForm(null, "");
        form.dataRow(row);
        form.addColumn("name_", "code_");
        assertEquals(
                """
                        <script>$(function() { initForm('#form1') });</script>
                        <form method='post' action='' id='form1' role='search' class='vuiForm'><ul>name_: <input type="text" name="name_" value="a01">code_: <input type="text" name="code_" value="001"></ul></form>""",
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
                        {"class":"VuiForm","id":"form1","bufferKey":"","action":"","dataRow":"","align":0,"enableConfig":true,"container":true,"visual":true,"components":[{"class":"FormStringField","id":"部门代码","title":"部门代码","field":"code_","mapSource":"","mark":"","placeholder":"","dialog":"","patten":"","readonly":false,"required":false,"autofocus":false,"expand":false,"visual":true},{"class":"FormStringField","id":"部门名称","title":"部门名称","field":"name_","mapSource":"","mark":"","placeholder":"","dialog":"","patten":"","readonly":false,"required":false,"autofocus":false,"expand":false,"visual":true}]}
                                """
                        .trim(),
                root.toString());
    }

    @Test
    public void test_searchButton() {
        var row = new DataRow();
        var form = new VuiForm(null, "");
        form.dataRow(row);
        SsrFormStyleDefault style = form.defaultStyle();
        form.addBlock(style.getSearchTextButton().field("search_text_"));
        assertEquals(
                """
                        <script>$(function() { initForm('#form1') });</script>
                        <form method='post' action='' id='form1' role='search' class='vuiForm'><div class="searchHead searchTextButton" >
                        <a role="configTemplate" class="hoverImageBox" type="button" onclick="showSsrConfigDialog('')">
                        <img src="images/icon/templateConfig.png" />
                        <img src="images/icon/templateConfig_hover.png" />
                        </a>
                        <li class="searchTextDiv">
                        <input type="text" name="search_text_" id="search_text_" value="${search_text_}" autocomplete="off" placeholder="请输入查询条件">
                        <span role="suffix-icon"></span>
                        </li>
                        <div class="searchFormButtonDiv">
                        <button name="submit" value="search">查询</button>
                        </div>
                        </div><ul></ul></form>""",
                form.toString());
    }

}
