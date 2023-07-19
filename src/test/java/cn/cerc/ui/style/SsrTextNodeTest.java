package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SsrTextNodeTest {

    @Test
    public void test_clearSpace() {
        assertEquals("", SsrUtils.fixSpace(""));
        assertEquals(" ", SsrUtils.fixSpace(" "));
        assertEquals(" ", SsrUtils.fixSpace("  "));
        assertEquals(" ", SsrUtils.fixSpace("   "));
        assertEquals("a", SsrUtils.fixSpace("a"));
        assertEquals(" a", SsrUtils.fixSpace(" a"));
        assertEquals(" a", SsrUtils.fixSpace("  a"));
        assertEquals("a ", SsrUtils.fixSpace("a "));
        assertEquals("a ", SsrUtils.fixSpace("a  "));
        assertEquals("a b", SsrUtils.fixSpace("a  b"));
        assertEquals(" a b ", SsrUtils.fixSpace("  a  b  "));
    }

    @Test
    public void test_1() {
        var text = "   a ";
        var node = new SsrTextNode(text);
        assertEquals(" a ", node.getHtml());
        assertEquals(" a ", node.getText());
    }

    @Test
    public void test_2() {
        var text = "a  ";
        var node = new SsrTextNode(text);
        assertEquals("a ", node.getHtml());
        assertEquals("a ", node.getText());
    }
}
