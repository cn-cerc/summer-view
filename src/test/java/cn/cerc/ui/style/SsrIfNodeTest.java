package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataSet;

public class SsrIfNodeTest {

    @Test
    public void test() {
        DataSet ds = new DataSet();
        ds.append().setValue("type", "1");
        ds.append().setValue("type", "2");

        var block = new UITemplateBlock(null, """
                ${dataset.begin}
                    ${if type==field_}
                        <span>第一个判断</span>
                    ${else}
                        <span>第二个判断</span>
                    ${endif}
                ${dataset.end}
                """);
        block.getTemplate().setDataSet(ds);
        block.getTemplate().toMap("field_", "1");
        assertEquals("<span>第一个判断</span><span>第二个判断</span>", block.toString());
    }

}
