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
    public void testDecode_row() {
        var template = new UITemplate("<div><span>${Code_}</span></div>");
        var result = template.decode(DataRow.of("Code_", "001"));
        assertEquals("<div><span>001</span></div>", result);
    }

    @Test
    public void testDecode_list() {
        var template = new UITemplate("<div>${list.begin}<span>${list.item}</span>${list.end}</div>");
        var result = template.decode(List.of("a1", "a2"));
        assertEquals("<div><span>a1</span><span>a2</span></div>", result);
    }

    @Test
    public void testDecode_map() {
        var template = new UITemplate("<div>${map.begin}<span>${map.key}:${map.value}</span>${map.end}</div>");
        var result = template.decode(Map.of("a", "b"));
        assertEquals("<div><span>a:b</span></div>", result);
    }

    @Test
    public void testDecode_dataset() {
        var template = new UITemplate("<div>${dataset.begin}<span>${Code_}</span>${dataset.end}</div>");
        var ds = new DataSet();
        ds.append().setValue("Code_", "001");
        ds.append().setValue("Code_", "002");
        var result = template.decode(ds);
        assertEquals("<div><span>001</span><span>002</span></div>", result);
    }

    private String margeList(List<UISsrNodeImpl> list) {
        var sb = new StringBuffer();
        for (var item : list)
            sb.append(item.getText()).append(",");
        return sb.toString();
    }

}
