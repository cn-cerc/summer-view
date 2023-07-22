package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cn.cerc.ui.core.UIComponent;

public class SsrDefineTest {

    @Test
    public void test_option() {
        var define = new SsrDefine("""
                ${define a _fields="code_,name_" width="10"}
                """);

        var ssr = define.get("a").get();

        assertEquals("", define.getOption("width").orElse(""));
        define.setOption("width", "30");
        assertEquals("10", ssr.getOption("width").orElse(""));
        assertEquals("30", define.getOption("width").orElse(""));

        assertTrue(define.isStrict());
        assertTrue(ssr.isStrict());
        ssr.setStrict(false);
        assertFalse(ssr.isStrict());
        assertTrue(define.isStrict());

        ssr.setOption("width", "20");
        assertEquals("20", ssr.getOption("width").orElse(""));
        assertEquals("30", define.getOption("width").orElse(""));

        assertEquals("a", ssr.id());

        assertEquals("code_,name_", ssr.getOption("_fields").orElse(""));
    }

    @Test
    public void test_base() {
        var define = new SsrDefine("""
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
            new UISsrBlock(root).setTemplate(ssr);
        }

        assertEquals("grid sample<table><tr><th>title</td></tr><tr><td>data<td></tr></table>--end--", root.toString());
    }

    @Test
    public void test_grid() {
        var define = new SsrDefine("""
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
            new UISsrBlock(root).setTemplate(ssr);
        assertEquals("grid sample<table v-data=\"1\"><tr><th>title</td></tr><tr><td>data<td></tr></table>--end--",
                root.toString());
    }

}
