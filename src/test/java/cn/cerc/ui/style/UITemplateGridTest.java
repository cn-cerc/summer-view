package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import cn.cerc.db.core.DataSet;

public class UITemplateGridTest {

    @Test
    public void test_default() {
        var ds = new DataSet();
        ds.append().setValue("code_", "001").setValue("name_", "a01");
        ds.append().setValue("code_", "002").setValue("name_", "b01");

        var grid = new UITemplateGrid(null, "");
        grid.putDefine(UITemplateGrid.TableBegin, "<table class='a'>");

        grid.putField("code_", "<td><a href=\"${url}\">${code_}</a></td>",
                (sender, field, template) -> template.toMap("url", "http://xxx"));

        grid.setDataSet(ds);
        assertEquals(
                "<table class='a'><tr><th>code_</th><th>name_</th></tr><tr><td><a href=\"http://xxx\">001</a></td><td>a01</td></tr><tr><td><a href=\"http://xxx\">002</a></td><td>b01</td></tr></table>",
                grid.toString());
    }

    @Test
    public void test_sample() {
        var grid = new UITemplateGrid(null, """
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
        grid.setDataSet(ds);
        grid.setFields(List.of("name", "code"));
        grid.onGetHead((sender, field, template) -> {
            if ("code".equals(field))
                template.toMap("title", "代码");
        });
        grid.onGetBody((sender, field, template) -> {
            if ("name".equals(field))
                template.toMap("url", "http://127.0.0.1");
        });

        assertEquals(
                "<table><tr class=\"head\"><th>姓名</th><th>代码</th></tr><tr><td><a href=\"http://127.0.0.1\">张三</a><td><td>001<td></tr><tr><td><a href=\"http://127.0.0.1\">李四</a><td><td>002<td></tr></table>",
                grid.toString());
    }

}
