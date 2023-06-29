package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;

public class UITemplateTest {

    @Test
    public void testDecodeString0() {
        var template = new UITemplate("");
        assertEquals("", margeList(template.getNodes()));
    }

    @Test
    public void testDecodeString1() {
        var template = new UITemplate("<div></div>");
        assertEquals("<div></div>,", margeList(template.getNodes()));
    }

    @Test
    public void testDecodeString2() {
        var template = new UITemplate("<div>${code.begin}aa${code.end}</div>");
        assertEquals("<div>,code.begin,aa,code.end,</div>,", margeList(template.getNodes()));
    }

    @Test
    public void testDecodeString3() {
        var template = new UITemplate("<div>${if year_}<span>${Code_}</span>${endif}</div>");
        assertEquals("<div>,if year_,</div>,", margeList(template.getNodes()));
    }

    @Test
    public void testDecodeString4() {
        var template = new UITemplate("${a}{abc}<div>${if year_}<span>${Code_}</span>${endif}</div>");
        assertEquals(4, template.getNodes().size());
        assertEquals("a,{abc}<div>,if year_,</div>,", margeList(template.getNodes()));
    }

    @Test
    public void testDecode_array() {
        var template = new UITemplate("<div><span>${0},${1}</span></div>");
        var result = template.setArray("001", "002").html();
        assertEquals("<div><span>001,002</span></div>", result);
    }

    @Test
    public void testDecode_row() {
        var template = new UITemplate("<div><span>${Code_}</span></div>");
        var result = template.setDataRow(DataRow.of("Code_", "001")).html();
        assertEquals("<div><span>001</span></div>", result);
    }

    @Test
    public void testDecode_list() {
        var template = new UITemplate("<div>${list.begin}<span>${list.item}</span>${list.end}</div>");
        var result = template.setList(List.of("a1", "a2")).html();
        assertEquals("<div><span>a1</span><span>a2</span></div>", result);
    }

    @Test
    public void testDecode_map() {
        var template = new UITemplate("<div>${map.begin}<span>${map.key}:${map.value}</span>${map.end}</div>");
        var result = template.setMap(Map.of("a", "b")).html();
        assertEquals("<div><span>a:b</span></div>", result);
    }

    @Test
    public void testDecode_dataset() {
        var template = new UITemplate("<div>${dataset.begin}<span>${Code_}</span>${dataset.end}</div>");
        var ds = new DataSet();
        ds.append().setValue("Code_", "001");
        ds.append().setValue("Code_", "002");
        var result = template.setDataSet(ds).html();
        assertEquals("<div><span>001</span><span>002</span></div>", result);
    }

    @Test
    public void testDecode_if_true() {
        var template = new UITemplate("<div>${if final_}<span>${code_}</span>${endif}</div>");
        var result = template.setDataRow(DataRow.of("final_", true, "code_", "001")).html();
        assertEquals("<div><span>001</span></div>", result);
    }

    @Test
    public void testDecode_if_false() {
        var template = new UITemplate("<div>${if final_}<span>ok</span>${endif}</div>");
        var result = template.setDataRow(DataRow.of("final_", false)).html();
        assertEquals("<div></div>", result);
    }

    @Test
    public void testDecode_combo() {
        var template = new UITemplate("<div>${0}${code_}${if final_}<span></span>${endif}${1}</div>");
        template.setArray("aaa", "bbb");
        template.setDataRow(DataRow.of("code_", "001", "final_", true));
        var result = template.html();
        assertEquals("<div>aaa001<span></span>bbb</div>", result);
    }
   
    private String margeList(List<UISsrNodeImpl> list) {
        var sb = new StringBuffer();
        for (var item : list)
            sb.append(item.getText()).append(",");
        return sb.toString();
    }

}
