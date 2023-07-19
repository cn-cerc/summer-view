package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SsrUtilsTest {

    @Test
    public void test_empty() {
        var nodes = SsrUtils.createNodes("""

                """);
        assertEquals(0, nodes.size());
    }

    @Test
    public void test_a() {
        var nodes = SsrUtils.createNodes("""
                a
                """);
        assertEquals(1, nodes.size());
        assertEquals("a", nodes.get(0).getText());
    }

}
