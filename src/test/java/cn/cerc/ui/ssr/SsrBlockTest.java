package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrNodes;

public class SsrBlockTest {

    @Test
    public void testDecodeString0() {
        var ssr = new SsrBlock("");
        assertEquals("", ssr.getHtml());
        ssr.setNodes(new SsrNodes("${0}-${list.index}"));
        ssr.toList("a");
        assertEquals("a-0", ssr.getHtml());
    }

    @Test
    public void testDecodeString01() {
        var template = new SsrBlock("${a}");
        template.option("a", "001");
        assertEquals("a,", margeList(template.nodes()));
        assertEquals("001", template.nodes().get(0).getHtml(template));
    }

    @Test
    public void testDecodeString1() {
        var template = new SsrBlock("<div></div>");
        assertEquals(",", margeList(template.nodes()));
    }

    @Test
    public void testDecodeString2() {
        var template = new SsrBlock("<div>${code.begin}aa${code.end}</div>");
        assertEquals(",code.begin,,code.end,,", margeList(template.nodes()));
    }

    @Test
    public void testDecodeString3() {
        var template = new SsrBlock("<div>${if year_}<span>${Code_}</span>${endif}</div>");
        assertEquals(",if year_,,", margeList(template.nodes()));
    }

    @Test
    public void testDecodeString4() {
        var template = new SsrBlock("${a}{abc}<div>${if year_}<span>${Code_}</span>${endif}</div>");
        assertEquals(4, template.nodes().size());
        assertEquals("a,,if year_,,", margeList(template.nodes()));
    }

    @Test
    public void testDecode_array() {
        var template = new SsrBlock("<div><span>${0},${1}</span></div>");
        var result = template.toList("001", "002").getHtml();
        assertEquals("<div><span>001,002</span></div>", result);
    }

    @Test
    public void testDecode_list() {
        var template = new SsrBlock("<div>${list.begin}<span>${list.item}</span>${list.end}</div>");
        var result = template.toList("a1", "a2").getHtml();
        assertEquals("<div><span>a1</span><span>a2</span></div>", result);
    }

    @Test
    public void testDecode_map() {
        var template = new SsrBlock("<div>${map.begin}<span>${map.key}:${map.value}</span>${map.end}</div>");
        var result = template.toMap("a", "b").getHtml();
        assertEquals("<div><span>a:b</span></div>", result);
    }

    @Test
    public void test_onGetText() {
        var ssr = new SsrBlock("""
                a
                ${1}
                b
                """);
        ssr.onGetValue((field, defaultValue) -> "1".equals(field) ? ":" : defaultValue);
        assertEquals("a:b", ssr.getHtml());
    }

    private String margeList(SsrNodes block) {
        var sb = new StringBuffer();
        for (var item : block)
            sb.append(item.getField()).append(",");
        return sb.toString();
    }

}
