package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataRow;

public class UISsrBlockTest {

    @Test
    public void test() {
        var block = new UISsrBlock(null, "<a href=${0}>${1}</a>");
        block.getTemplate().toList("url", "name");
        assertEquals("<a href=url>name</a>", block.toString());
    }

    @Test
    public void test_Strict_1() {
        var block1 = new UISsrBlock(null, "${0}${1}");
        block1.getTemplate().toList("a");
        assertEquals("a${1}", block1.toString());

        var block2 = new UISsrBlock(null, "${code}${name}");
        block2.getTemplate().setDataRow(DataRow.of("code", "a"));
        assertEquals("a${name}", block2.toString());
    }

    @Test
    public void test_Strict_2() {
        var block1 = new UISsrBlock(null, "${0}${1}");
        block1.getTemplate().toList("a").strict(false);
        assertEquals("a", block1.toString());

        var block2 = new UISsrBlock(null, "${code}${name}");
        block2.getTemplate().setDataRow(DataRow.of("code", "a")).strict(false);
        assertEquals("a", block2.toString());
    }

}
