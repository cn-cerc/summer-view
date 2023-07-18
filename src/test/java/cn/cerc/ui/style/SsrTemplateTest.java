package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import cn.cerc.db.core.DataRow;

public class SsrTemplateTest {

    @Test
    public void testDecodeString0() {
        var template = new SsrTemplate("");
        assertEquals("", margeList(template.getNodes()));
    }

    @Test
    public void testDecodeString01() {
        var template = new SsrTemplate("${a}");
        template.getOptions().put("a", "001");
        assertEquals("a,", margeList(template.getNodes()));
        assertEquals("001", template.getNodes().get(0).getHtml());
    }

    @Test
    public void testDecodeString1() {
        var template = new SsrTemplate("<div></div>");
        assertEquals(",", margeList(template.getNodes()));
    }

    @Test
    public void testDecodeString2() {
        var template = new SsrTemplate("<div>${code.begin}aa${code.end}</div>");
        assertEquals(",code.begin,,code.end,,", margeList(template.getNodes()));
    }

    @Test
    public void testDecodeString3() {
        var template = new SsrTemplate("<div>${if year_}<span>${Code_}</span>${endif}</div>");
        assertEquals(",if year_,,", margeList(template.getNodes()));
    }

    @Test
    public void testDecodeString4() {
        var template = new SsrTemplate("${a}{abc}<div>${if year_}<span>${Code_}</span>${endif}</div>");
        assertEquals(4, template.getNodes().size());
        assertEquals("a,,if year_,,", margeList(template.getNodes()));
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
    public void test_Strict_1() {
        var block1 = new UISsrBlock(null, "${0}${1}");
        block1.getTemplate().toList("a");
        assertEquals("a${1}", block1.toString());

        var block2 = new UISsrBlock(null, "${code}${name}");
        block2.getTemplate().setDataRow(DataRow.of("code", "a"));
        assertEquals("a${name}", block2.toString());
    }

    @Test
    public void test_Strict_2() {
        var block1 = new UISsrBlock(null, "${0}${1}");
        block1.getTemplate().toList("a").setStrict(false);
        assertEquals("a", block1.toString());

        var block2 = new UISsrBlock(null, "${code}${name}");
        block2.getTemplate().setDataRow(DataRow.of("code", "a")).setStrict(false);
        assertEquals("a", block2.toString());
    }

    private String margeList(List<SsrNodeImpl> list) {
        var sb = new StringBuffer();
        for (var item : list)
            sb.append(item.getField()).append(",");
        return sb.toString();
    }

}
