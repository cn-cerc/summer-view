package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.ui.core.UIComponent;

public class SsrDefineTest {

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
            var value = define.get(key);
            if (value.isPresent()) {
                new UISsrBlock(root).setTemplate(value.get());
            }
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
