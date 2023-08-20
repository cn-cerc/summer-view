package cn.cerc.ui.ssr.grid;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataSet;

public class VuiGridTest {

    @Test
    public void test_base() {
        var ds = new DataSet();
        ds.append().setValue("code_", "001").setValue("name_", "a01");
        ds.append().setValue("code_", "002").setValue("name_", "b01");
        var grid = new VuiGrid(null, "");
        grid.dataSet(ds);
        grid.addColumn("name_");
        assertEquals("""
                <div id='grid' class='scrollArea'><table class='dbgrid'><tr>
                <th>name_</th></tr>
                <tr>
                <td>a01</td></tr>
                <tr>
                <td>b01</td></tr>
                </table></div>""", grid.toString());
    }

    @Test
    public void test_default() {
        var ds = new DataSet();
        ds.append().setValue("code_", "001").setValue("name_", "a01");
        ds.append().setValue("code_", "002").setValue("name_", "b01");

        var grid = new VuiGrid(null, "");
        grid.addBlock(VuiGrid.TableBegin, "<div><table class='a'>");
        grid.addBlock("head.code_", "<th width=${width}>${title}</td>");
        grid.onGetHeadHtml("code_", ssr -> ssr.toMap("width", "30").toMap("title", "xxx"));
        grid.addBlock("body.code_", "<td><a href=\"${url}\">${code_}</a></td>");
        grid.onGetBodyHtml("code_", ssr -> ssr.toMap("url", "http://" + ds.getString(ssr.id())));
        grid.addColumn("code_", "name_");

        grid.dataSet(ds);
        assertEquals("""
                <div><table class='a'><tr>
                <th width=30>xxx</td><th>name_</th></tr>
                <tr>
                <td><a href="http://001">001</a></td><td>a01</td></tr>
                <tr>
                <td><a href="http://002">002</a></td><td>b01</td></tr>
                </table></div>""", grid.toString());
    }

    @Test
    public void test_sample() {
        var grid = new VuiGrid(null, """
                ${define table.begin}
                <table>

                ${define head.begin}
                <tr class="head">

                ${define head.code title=""}
                <th>${title}</th>

                ${define head.name title="姓名"}
                <th>姓名</th>

                ${define head.end}
                </tr>

                ${define body.begin}
                <tr>

                ${define body.code}
                <td>${code}<td>

                ${define body.name}
                <td><a href="${url}">${name}</a><td>

                ${define body.end}
                </tr>

                ${define table.end}
                </table>

                ${define end}
                """);
        DataSet ds = new DataSet();
        ds.fields().add("code").setName("代码");
        ds.fields().add("name").setName("姓名");
        ds.append().setValue("code", "001").setValue("name", "张三");
        ds.append().setValue("code", "002").setValue("name", "李四");
        grid.dataSet(ds);
        grid.addColumn("name", "code");
        grid.onGetHeadHtml("code", ssr -> ssr.toMap("title", "代码"));
        grid.onGetBodyHtml("name", ssr -> ssr.toMap("url", "http://127.0.0.1"));

        assertEquals("""
                <table><tr class="head">
                <th>姓名</th><th>代码</th></tr>
                <tr>
                <td><a href="http://127.0.0.1">张三</a><td><td>001<td></tr>
                <tr>
                <td><a href="http://127.0.0.1">李四</a><td><td>002<td></tr>
                </table>""", grid.toString());
    }

}
