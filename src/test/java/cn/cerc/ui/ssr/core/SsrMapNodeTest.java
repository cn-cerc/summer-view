package cn.cerc.ui.ssr.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataSet;

public class SsrMapNodeTest {

    @Test
    public void test_base() {
        var ssr = new SsrBlock(
                """
                        <select>
                        ${map.begin}
                        <option value ="${map.key}" ${if map.key==Code_}selected${endif}>${map.index}:${map.value}</option>${map.end}
                        </select>
                        """);
        ssr.toMap("a", "张三");
        ssr.toMap("b", "李四");
        ssr.toMap("c", "王五");
        var ds = new DataSet();
        ds.append().setValue("Code_", "a");

        ssr.dataSet(ds);
        assertEquals(
                "<select><option value =\"a\" selected>0:张三</option><option value =\"b\" >1:李四</option><option value =\"c\" >2:王五</option></select>",
                ssr.html());
    }

}
