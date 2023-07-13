package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import cn.cerc.db.core.DataRow;

public class SsrTemplateTest {

    @Test
    public void testDecodeString0() {
        var template = new SsrTemplate("");
        assertEquals("", margeList(template.getNodes()));
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
        var result = template.setList(List.of("001", "002")).getHtml();
        assertEquals("<div><span>001,002</span></div>", result);
    }

    @Test
    public void testDecode_if0() {
        var template = new SsrTemplate("""
                <div>
                    ${if Code_==1}
                    yes
                    ${else}
                    no
                    ${endif}
                </div>
                """);
        var result1 = template.setDataRow(DataRow.of("Code_", 1)).getHtml();
        assertEquals("<div>yes</div>", result1);
        var result2 = template.setDataRow(DataRow.of("Code_", 2)).getHtml();
        assertEquals("<div>no</div>", result2);
    }

    @Test
    public void testDecode_if1() {
        var template = new SsrTemplate("""
                <div>
                    ${if Code_=='001'}
                    yes
                    ${else}
                    no
                    ${endif}
                </div>
                """);
        var result1 = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div>yes</div>", result1);
        var result2 = template.setDataRow(DataRow.of("Code_", "002")).getHtml();
        assertEquals("<div>no</div>", result2);
    }

    @Test
    public void testDecode_if3() {
        var template = new SsrTemplate("""
                <div>
                    ${if Code_!='002'}
                    <span>${Code_}</span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div><span>001</span></div>", result);
    }

    @Test
    public void testDecode_if4() {
        var template = new SsrTemplate("""
                <div>
                    ${if Code_<>'002'}
                    <span>${Code_}</span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div><span>001</span></div>", result);
    }

    @Test
    public void testDecode_if5() {
        var template = new SsrTemplate("""
                <div>
                    ${if Code_>='000'}
                    <span></span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div><span></span></div>", result);
    }

    @Test
    public void testDecode_if6() {
        var template = new SsrTemplate("""
                <div>
                    ${if Code_<='002'}
                    <span></span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div><span></span></div>", result);
    }

    @Test
    public void testDecode_if7() {
        var template = new SsrTemplate("""
                <div>
                    ${if Code_<'002'}
                    <span></span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div><span></span></div>", result);
    }

    @Test
    public void testDecode_if8() {
        var template = new SsrTemplate("""
                <div>
                    ${if Code_>'0'}
                    <span></span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div><span></span></div>", result);
    }

    @Test
    public void testDecode_if9() {
        var template = new SsrTemplate("""
                <div>
                    ${if Code_ is empty}
                    <span></span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "")).getHtml();
        assertEquals("<div><span></span></div>", result);
    }

    @Test
    public void testDecode_if10() {
        var template = new SsrTemplate("""
                <div>
                    ${if Code_ is not empty}
                    <span></span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div><span></span></div>", result);
    }

    @Test
    public void testDecode_if_true() {
        var template = new SsrTemplate("<div>${if final_}<span>${code_}</span>${endif}</div>");
        var result = template.setDataRow(DataRow.of("final_", true, "code_", "001")).getHtml();
        assertEquals("<div><span>001</span></div>", result);
    }

    @Test
    public void testDecode_if_false() {
        var template = new SsrTemplate("<div>${if final_}<span>ok</span>${endif}</div>");
        var result = template.setDataRow(DataRow.of("final_", false)).getHtml();
        assertEquals("<div></div>", result);
    }

    @Test
    public void testDecode_list() {
        var template = new SsrTemplate("<div>${list.begin}<span>${list.item}</span>${list.end}</div>");
        var result = template.setList(List.of("a1", "a2")).getHtml();
        assertEquals("<div><span>a1</span><span>a2</span></div>", result);
    }

    @Test
    public void testDecode_map() {
        var template = new SsrTemplate("<div>${map.begin}<span>${map.key}:${map.value}</span>${map.end}</div>");
        var result = template.setMap(Map.of("a", "b")).getHtml();
        assertEquals("<div><span>a:b</span></div>", result);
    }

    @Test
    public void testDecode_combo() {
        var template = new SsrTemplate("""
                <div>
                ${0}${code_}
                ${if Ready_}
                    <span>from map</span>
                ${endif}
                ${if final_}
                    <span>from row</span>
                ${endif}
                ${1}
                </div>""");
        template.setList(List.of("aaa", "bbb"));
        template.setMap(Map.of("Ready_", "true"));
        template.setDataRow(DataRow.of("code_", "001", "final_", true));
        var result = template.getHtml();
        assertEquals("<div>aaa001<span>from map</span><span>from row</span>bbb</div>", result);
    }

    @Test
    public void test_Strict_1() {
        var block1 = new UITemplateBlock(null, "${0}${1}");
        block1.getTemplate().setList(List.of("a"));
        assertEquals("a${1}", block1.toString());

        var block2 = new UITemplateBlock(null, "${code}${name}");
        block2.getTemplate().setDataRow(DataRow.of("code", "a"));
        assertEquals("a${name}", block2.toString());
    }

    @Test
    public void test_Strict_2() {
        var block1 = new UITemplateBlock(null, "${0}${1}");
        block1.getTemplate().setList(List.of("a")).setStrict(false);
        assertEquals("a", block1.toString());

        var block2 = new UITemplateBlock(null, "${code}${name}");
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
