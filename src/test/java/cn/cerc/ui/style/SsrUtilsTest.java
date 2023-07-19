package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SsrUtilsTest {

    @Test
    public void test_aa() {
        var text = """
                    ${if  CreateMode}
                    ${if output}
                        ok
                    ${endif}
                ${endif}
                """;
        var ssr = new SsrTemplate("");
        var nodes = SsrUtils.createNodes(text);
        nodes.forEach(node -> node.setTemplate(ssr));
        System.out.println(nodes.size());
        for (var node : nodes) {
            System.out.println(String.format("%s: %s", node.getClass().getSimpleName(), node.getHtml()));
        }
    }

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
        assertEquals("template is null", node.getHtml());

        node = nodes.get(1);
        assertEquals("", node.getField());
        assertEquals("a", node.getText());
        assertEquals("a", node.getHtml());

        node = nodes.get(2);
        assertEquals("bbb", node.getField());
        assertEquals("${bbb}", node.getText());
        assertEquals("template is null", node.getHtml());
    }

}
