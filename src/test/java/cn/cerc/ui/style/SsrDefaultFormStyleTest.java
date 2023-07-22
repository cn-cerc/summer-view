package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import cn.cerc.db.core.DataRow;

public class SsrDefaultFormStyleTest {

    @Test
    public void test() {
        var form = new UISsrForm(null);
        form.setStrict(false);
        form.setDataRow(DataRow.of("Name_", "jason", "Man_", true));

        var style = form.createDefaultStyle();
        form.addTemplate(style.getTextBox("姓名", "Name_"));
        form.addTemplate(style.getCheckBox("男性", "Man_"));
        form.addTemplate(style.getListBox("学历", "Xl_", List.of("小学", "初中", "高中"), "初中"));
        form.addField("姓名", "男性", "学历");

        assertEquals(
                """
                        <form method='post' action=''>姓名: <input type="text" name="Name_" value="jason" /><input type="checkbox" name="Man_" value="true" checked/>男性<select name="Xl_"><option value="小学" >小学</option><option value="初中" selected>初中</option><option value="高中" >高中</option></select></form>
                        """
                        .trim(),
                form.toString());
    }

}
