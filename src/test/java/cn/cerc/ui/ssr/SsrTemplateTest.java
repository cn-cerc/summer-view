package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cn.cerc.ui.core.UIComponent;

public class SsrTemplateTest {

    @Test
    public void test_option() {
        var template = new SsrTemplate("""
                ${define a _fields="code_,name_" width="10"}
                """);

        var ssr = template.get("a").get();

        assertEquals("", template.option("width").orElse(""));
        template.option("width", "30");
        assertEquals("10", ssr.option("width").orElse(""));
        assertEquals("30", template.option("width").orElse(""));

        assertTrue(template.strict());
        assertTrue(ssr.strict());
        ssr.strict(false);
        assertFalse(ssr.strict());
        assertTrue(template.strict());

        ssr.option("width", "20");
        assertEquals("20", ssr.option("width").orElse(""));
        assertEquals("30", template.option("width").orElse(""));

        assertEquals("a", ssr.id());

        assertEquals("code_,name_", ssr.option("_fields").orElse(""));
    }

    @Test
    public void test_base() {
        var define = new SsrTemplate("""
                grid sample
                ${define table.begin}
                <table>
                    ${define head.begin}
                    <tr>
                        ${define head.data}
                        <th>title</td>
                        ${define head.end}
                    </tr>
                    ${define body.begin}
                    <tr>
                    ${define body.data}
                    <td>data<td>
                    ${define body.end}
                    </tr>
                ${define table.end}
                </table>
                ${define end}
                --end--
                """);

        var root = new UIComponent(null);
        for (var ssr : define) {
            new UISsrBlock(root).block(ssr);
        }

        assertEquals("grid sample<table><tr><th>title</td></tr><tr><td>data<td></tr></table>--end--", root.toString());
    }

    @Test
    public void test_grid() {
        var define = new SsrTemplate("""
                grid sample
                ${define table.begin a="1"}
                <table v-data="${a}">
                    ${define head.begin}
                    <tr>
                        ${define head.data}
                        <th>title</td>
                        ${define head.end}
                    </tr>
                    ${define body.begin}
                    <tr>
                    ${define body.data}
                    <td>data<td>
                    ${define body.end}
                    </tr>
                ${define table.end}
                </table>
                ${define end}
                --end--
                """);

        var root = new UIComponent(null);
        for (var ssr : define)
            new UISsrBlock(root).block(ssr);
        assertEquals("grid sample<table v-data=\"1\"><tr><th>title</td></tr><tr><td>data<td></tr></table>--end--",
                root.toString());
    }

}
