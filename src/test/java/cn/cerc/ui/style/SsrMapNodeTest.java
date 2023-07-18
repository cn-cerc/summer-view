package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataSet;

public class SsrMapNodeTest {

    @Test
    public void test() {
        var ssr = new SsrTemplate("""
                <select>
                ${map.begin}
                <option value ="${map.key}" ${if map.key==Code_}selected${endif}>${map.value}</option>${map.end}
                </select>
                """);
        ssr.toMap("a", "张三");
        ssr.toMap("b", "李四");
        ssr.toMap("c", "王五");
        var ds = new DataSet();
        ds.append().setValue("Code_", "a");

        ssr.setDataSet(ds);
        assertEquals(
                "<select><option value =\"a\" selected>张三</option><option value =\"b\" >李四</option><option value =\"c\" >王五</option></select>",
                ssr.getHtml());
    }

}
