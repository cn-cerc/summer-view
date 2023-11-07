package cn.cerc.ui.ssr.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SsrUtilsTest {

    @Test
    public void test_clearSpace() {
        assertEquals("", SsrUtils.fixSpace(""));
        assertEquals(" ", SsrUtils.fixSpace(" "));
        assertEquals(" ", SsrUtils.fixSpace("  "));
        assertEquals(" ", SsrUtils.fixSpace("   "));
        assertEquals("a", SsrUtils.fixSpace("a"));
        assertEquals("a", SsrUtils.fixSpace(" a"));
        assertEquals("a", SsrUtils.fixSpace("  a"));
        assertEquals("a ", SsrUtils.fixSpace("a "));
        assertEquals("a ", SsrUtils.fixSpace("a  "));
        assertEquals("a b", SsrUtils.fixSpace("a  b"));
        assertEquals("a b ", SsrUtils.fixSpace("  a  b  "));
        assertEquals("a\nb\nc ", SsrUtils.fixSpace(" \n a \n  b \n  c  \n "));
    }

    @Test
    public void test_empty() {
        var nodes = SsrUtils.createNodes("""
                       ${a}
                       abc
                       a
                       b
                ${b}
                       """);
        assertEquals(3, nodes.size());
    }

    @Test
    public void test_a() {
        var nodes = SsrUtils.createNodes("""
                a
                ok
                """);
        assertEquals(1, nodes.size());
        assertEquals("a\nok", nodes.get(0).getText());
    }

    @Test
    public void test_b() {
        var nodes = SsrUtils.createNodes("""
                ${abc}
                a
                ${bbb}b
                """);
        assertEquals(4, nodes.size());

        var node = nodes.get(0);
        assertEquals("abc", node.getField());
        assertEquals("${abc}", node.getText());
        assertEquals("block is null", node.getHtml(null));

        node = nodes.get(1);
        assertEquals("", node.getField());
        assertEquals("a", node.getText());
        assertEquals("a", node.getHtml(null));

        node = nodes.get(2);
        assertEquals("bbb", node.getField());
        assertEquals("${bbb}", node.getText());
        assertEquals("block is null", node.getHtml(null));
    }

    @Test
    public void test_extractTagContent() {
        assertEquals("单元格", SsrUtils.extractTagContent("<td>单元格</td>", "td"));
        assertEquals("单元格", SsrUtils.extractTagContent("<td role='remark_' align='center' >单元格</td>", "td"));
        assertEquals("<span>单元格</span>",
                SsrUtils.extractTagContent("<td role='remark_' align='center' ><span>单元格</span></td>", "td"));
        assertEquals("<a href=''>单元格</a>",
                SsrUtils.extractTagContent("<td role='remark_' align='center' ><a href=''>单元格</a></td>", "td"));
        assertEquals("<a href=''>单元格</a>",
                SsrUtils.extractTagContent("<div role='remark_' align='center' ><a href=''>单元格</a></div>", "div"));
        assertEquals("""

                    <a href=''>单元格</a>
                """, SsrUtils.extractTagContent("""
                <div role='remark_' align='center' >
                    <a href=''>单元格</a>
                </div>""", "div"));
    }

}
