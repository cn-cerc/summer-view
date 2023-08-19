package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataRow;
import cn.cerc.ui.ssr.core.SsrBlock;

public class SsrDataRowTest {
    @Test
    public void test_space() {
        var template = new SsrBlock("""
                <div>
                ${if Code_<>'002'}
                    <span ${Code_}>a</span>
                    ${endif}
                </div>
                """);
        var result = template.setDataRow(DataRow.of("Code_", "001")).getHtml();
        assertEquals("<div><span 001>a</span> </div>", result);
    }
}
