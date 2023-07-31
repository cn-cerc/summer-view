package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;

public class SsrIfNodeTest {

    @Test
    public void test_not_if() {
        var template = new SsrBlock("""
                ${if not final}
                ok
                ${endif}
                ${if ready}
                yes
                ${endif}
                """);
        template.toMap("final", "false");
        template.toMap("ready", "true");
        assertEquals("okyes", template.getHtml());
    }

    @Test
    public void test_1() {
        DataSet ds = new DataSet();
        ds.append().setValue("type", "1");
        ds.append().setValue("type", "2");

        var block = new UISsrBlock(null, """
                ${dataset.begin}
                    ${if type==field_}
                        <span>第一个判断</span>
                    ${else}
                        <span>第二个判断</span>
                    ${endif}
                ${dataset.end}
                """);
        block.block().setDataSet(ds);
        block.block().toMap("field_", "1");
        assertEquals(" <span>第一个判断</span>  <span>第二个判断</span> ", block.toString());
    }

    @Test
    public void testDecode_if0() {
        var template = new SsrBlock("""
                <div>
                    ${if Code_==1 }yes
                    ${else}no
                    ${endif}
                </div>
                """);
        var result1 = template.setDataRow(DataRow.of("Code_", 1)).getHtml();
        assertEquals("<div> yes </div>", result1);
        var result2 = template.setDataRow(DataRow.of("Code_", 2)).getHtml();
        assertEquals("<div> no </div>", result2);
    }

    @Test
    public void testDecode_if1() {
        var template = new SsrBlock("""
                <div>
                    ${if Code_=='001' }
                    yes
                    ${else}
                    no
                    ${endif}
                </div>
                """);
        var result1 = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div> yes </div>", result1);
        var result2 = template.setDataRow(DataRow.of("Code_", "002")).getHtml();
        assertEquals("<div> no </div>", result2);
    }

    @Test
    public void testDecode_if3() {
        var template = new SsrBlock("""
                <div>
                    ${if Code_!='002'}
                    <span>${Code_}</span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div> <span>001</span> </div>", result);
    }

    @Test
    public void testDecode_if5() {
        var template = new SsrBlock("""
                <div>
                    ${if Code_>='000'}
                    <span></span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div> <span></span> </div>", result);
    }

    @Test
    public void testDecode_if6() {
        var template = new SsrBlock("""
                <div>
                    ${if Code_<='002'}
                    <span></span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div> <span></span> </div>", result);
    }

    @Test
    public void testDecode_if7() {
        var template = new SsrBlock("""
                <div>
                    ${if Code_<'002'}
                    <span></span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div> <span></span> </div>", result);
    }

    @Test
    public void testDecode_if8() {
        var template = new SsrBlock("""
                <div>
                    ${if Code_>'0'}
                    <span></span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div> <span></span> </div>", result);
    }

    @Test
    public void testDecode_if9() {
        var template = new SsrBlock("""
                <div>
                    ${if Code_ is empty}
                    <span></span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "")).getHtml();
        assertEquals("<div> <span></span> </div>", result);
    }

    @Test
    public void testDecode_if10() {
        var template = new SsrBlock("""
                <div>
                    ${if Code_ is not empty}
                    <span></span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div> <span></span> </div>", result);
    }

    @Test
    public void testDecode_if_true() {
        var template = new SsrBlock("<div>${if final_}<span>${code_}</span>${endif}</div>");
        var result = template.setDataRow(DataRow.of("final_", true, "code_", "001")).getHtml();
        assertEquals("<div><span>001</span></div>", result);
    }

    @Test
    public void testDecode_if_false() {
        var template = new SsrBlock("<div>${if final_}<span>ok</span>${endif}</div>");
        var result = template.setDataRow(DataRow.of("final_", false)).getHtml();
        assertEquals("<div></div>", result);
    }
}
