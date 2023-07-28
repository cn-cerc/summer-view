package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SsrTemplateTest {

    @Test
    public void testDecodeString0() {
        var ssr = new SsrTemplate("");
        assertEquals("", ssr.getHtml());
        ssr.setStyle(new SsrStyle("${0}-${list.index}"));
        ssr.toList("a");
        assertEquals("a-0", ssr.getHtml());
    }

    @Test
    public void testDecodeString01() {
        var template = new SsrTemplate("${a}");
        template.option("a", "001");
        assertEquals("a,", margeList(template.style()));
        assertEquals("001", template.style().get(0).getHtml(template));
    }

    @Test
    public void testDecodeString1() {
        var template = new SsrTemplate("<div></div>");
        assertEquals(",", margeList(template.style()));
    }

    @Test
    public void testDecodeString2() {
        var template = new SsrTemplate("<div>${code.begin}aa${code.end}</div>");
        assertEquals(",code.begin,,code.end,,", margeList(template.style()));
    }

    @Test
    public void testDecodeString3() {
        var template = new SsrTemplate("<div>${if year_}<span>${Code_}</span>${endif}</div>");
        assertEquals(",if year_,,", margeList(template.style()));
    }

    @Test
    public void testDecodeString4() {
        var template = new SsrTemplate("${a}{abc}<div>${if year_}<span>${Code_}</span>${endif}</div>");
        assertEquals(4, template.style().size());
        assertEquals("a,,if year_,,", margeList(template.style()));
    }

    @Test
    public void testDecode_array() {
        var template = new SsrTemplate("<div><span>${0},${1}</span></div>");
        var result = template.toList("001", "002").getHtml();
        assertEquals("<div><span>001,002</span></div>", result);
    }

    @Test
    public void testDecode_list() {
        var template = new SsrTemplate("<div>${list.begin}<span>${list.item}</span>${list.end}</div>");
        var result = template.toList("a1", "a2").getHtml();
        assertEquals("<div><span>a1</span><span>a2</span></div>", result);
    }

    @Test
    public void testDecode_map() {
        var template = new SsrTemplate("<div>${map.begin}<span>${map.key}:${map.value}</span>${map.end}</div>");
        var result = template.toMap("a", "b").getHtml();
        assertEquals("<div><span>a:b</span></div>", result);
    }

    @Test
    public void test_onGetText() {
        var ssr = new SsrTemplate("""
                a
                ${1}
                b
                """);
        ssr.onGetValue((field, defaultValue) -> "1".equals(field) ? ":" : defaultValue);
        assertEquals("a:b", ssr.getHtml());
    }

    private String margeList(SsrStyle block) {
        var sb = new StringBuffer();
        for (var item : block)
            sb.append(item.getField()).append(",");
        return sb.toString();
    }

}
