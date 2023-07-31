package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SsrTextNodeTest {

    @Test
    public void test_1() {
        var text = "   a ";
        var node = new SsrTextNode(text);
        assertEquals("a ", node.getHtml(null));
        assertEquals("a ", node.getText());
    }

    @Test
    public void test_2() {
        var text = "a  ";
        var node = new SsrTextNode(text);
        assertEquals("a ", node.getHtml(null));
        assertEquals("a ", node.getText());
    }
}
