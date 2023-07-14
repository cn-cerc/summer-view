package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;

public class SsrDatasetTest {

    @Test
    public void testDecode_dataset1() {
        var template = new SsrTemplate("""
                <div>${dataset.begin}
                ${dataset.rec}
                ${if Final_}
                    ${TBNo_}
                    <span>${Code_}</span>
                ${else}
                    <span>else</span>
                ${endif}
                ${dataset.end}</div>
                """);
        var ds = new DataSet();
        ds.append().setValue("Code_", "001").setValue("Final_", true);
        ds.append().setValue("Code_", "002").setValue("Final_", true);
        ds.append().setValue("Code_", "003");
        template.setDataRow(DataRow.of("TBNo_", "OD001"));
        var result = template.setDataSet(ds).getHtml();
        assertEquals("<div>1 OD001<span>001</span>2 OD001<span>002</span>3<span>else</span></div>", result);
    }

    @Test
    public void testDecode_dataset2() {
        var template = new SsrTemplate("""
                <div>${dataset.begin}
                ${if Final_}
                    ${TBNo_}
                    <span>${dataset.Code_}</span>
                ${else}
                    <span>else</span>
                ${endif}
                ${dataset.end}</div>
                """);
        var ds = new DataSet();
        ds.append().setValue("Code_", "001").setValue("Final_", true);
        ds.append().setValue("Code_", "002").setValue("Final_", true);
        ds.append().setValue("Code_", "003");
        template.setDataRow(DataRow.of("TBNo_", "OD001"));
        var result = template.setDataSet(ds).getHtml();
        assertEquals("<div> OD001<span>001</span> OD001<span>002</span><span>else</span></div>", result);
    }

}
