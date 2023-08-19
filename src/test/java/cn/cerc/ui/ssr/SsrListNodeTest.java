package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataRow;
import cn.cerc.ui.ssr.core.SsrBlock;

public class SsrListNodeTest {

    @Test
    public void test() {
        var ssr = new SsrBlock("""
                ${list.begin}
                <span>${list.index}:${list.value}</span>
                ${list.end}
                """);
        ssr.toList("a", "b");
        assertEquals("<span>0:a</span><span>1:b</span>", ssr.getHtml());
    }

    @Test
    public void test_a() {
        var ssr = new SsrBlock("""
                ${list.begin}
                ${if list.item==Code_}selected${endif}
                ${list.end}
                """);
        ssr.setDataRow(DataRow.of("Code_", "a"));
        ssr.toList("a", "b");
        assertEquals("selected", ssr.getHtml());
    }

}
