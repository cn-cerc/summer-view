package cn.cerc.ui.ssr.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataRow;

public class SsrComboTest {

    @Test
    public void test_empty() {
        SsrBlock ssr = new SsrBlock("""
                a  ${if CreateMode}
                    ${if output}
                        ok
                    ${endif}
                ${endif}
                """);
        ssr.toMap("CreateMode", "true").toMap("output", "true");
        assertEquals("a  ok ", ssr.html());
    }

    @Test
    public void testDecode_combo() {
        var template = new SsrBlock("""
                <div>
                ${0}${code_}
                ${if Ready_}
                    <span>from map</span>
                ${endif}
                ${if final_}
                    <span>from row</span>
                    ${if status_}
                    3-
                    ${endif}
                ${endif}
                ${1}
                </div>""");
        template.toList("aaa", "bbb");
        template.toMap("Ready_", "true");
        template.toMap("status_", "true");
        template.dataRow(DataRow.of("code_", "001", "final_", true));
        var result = template.html();
        assertEquals("<div>aaa001<span>from map</span><span>from row</span> 3- bbb</div>", result);
    }

}
