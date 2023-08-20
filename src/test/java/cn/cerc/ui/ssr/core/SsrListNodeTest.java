package cn.cerc.ui.ssr.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataRow;

public class SsrListNodeTest {

    @Test
    public void test() {
        var ssr = new SsrBlock("""
                ${list.begin}
                <span>${list.index}:${list.value}</span>
                ${list.end}
                """);
        ssr.toList("a", "b");
        assertEquals("<span>0:a</span><span>1:b</span>", ssr.html());
    }

    @Test
    public void test_a() {
        var ssr = new SsrBlock("""
                ${list.begin}
                ${if list.item==Code_}selected${endif}
                ${list.end}
                """);
        ssr.dataRow(DataRow.of("Code_", "a"));
        ssr.toList("a", "b");
        assertEquals("selected", ssr.html());
    }

}
