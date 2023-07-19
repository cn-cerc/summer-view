package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataRow;

public class SsrListNodeTest {

    @Test
    public void test() {
        var ssr = new SsrTemplate("""
                ${list.begin}
                ${if list.item==Code_}selected${endif}
                ${list.end}
                """);
        ssr.setDataRow(DataRow.of("Code_", "a"));
        ssr.toList("a", "b");
        assertEquals("selected", ssr.getHtml());
    }

}
