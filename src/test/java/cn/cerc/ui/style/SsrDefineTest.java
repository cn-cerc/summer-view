package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.ui.core.UIComponent;

public class SsrDefineTest {

    @Test
    public void test_option() {
        var define = new SsrDefine("""
                ${define a _fields="code_,name_" width="10"}
                """);

        assertEquals(1, define.get("a").get().getMap().size());
        assertEquals(2, define.getOption("a").size());

        assertEquals("10", define.get("a").get().getMap().get("width"));
        assertEquals("10", define.getOption("a").get("width"));

        // 带下划线的只会进入到 option 中
        assertEquals(null, define.get("a").get().getMap().get("_fields"));
        assertEquals("code_,name_", define.getOption("a").get("_fields"));
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
        for (var key : define.items().keySet()) {
            var ssr = define.get(key).get();
            System.out.println(ssr.id());
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
        for (var key : define.items().keySet()) {
            var value = define.get(key);
            if (value.isPresent()) {
                new UISsrBlock(root).setTemplate(value.get());
            }
        }
        assertEquals("grid sample<table v-data=\"1\"><tr><th>title</td></tr><tr><td>data<td></tr></table>--end--",
                root.toString());
    }

}
