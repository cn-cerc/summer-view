package cn.cerc.ui.style;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataRow;

public class SsrDefaultFormStyleTest {

    @Test
    public void test() {
        var form = new UISsrForm(null);
        form.setStrict(false);
        form.setDataRow(DataRow.of("Name_", "jason", "Man_", true, "Xl_", "初中"));

        var style = form.createDefaultStyle();
        form.addTemplate(style.getString("姓名", "Name_"));
        form.addTemplate(style.getBoolean("男性", "Man_"));
        var ssr = form.addTemplate(style.getMap("学历", "Xl_"));
        ssr.toMap("小学", "小学");
        ssr.toMap("初中", "初中");
        ssr.toMap("高中", "高中");
        form.addField("姓名", "男性", "学历");

        assertEquals(
                """
                        <form method='post' action=''><ul><li>
                        <label for="Name_"><em>姓名</em></label>
                        <div>
                        <input autocomplete="off" name="Name_" id="Name_" type="text" value="jason"     />
                        <span role="suffix-icon"></span>
                        </div>
                        </li><li>
                        <div role="switch">
                        <input autocomplete="off" name="Man_" id="Man_" type="checkbox" value="1" checked />
                        </div>
                        <label for="Man_"><em>男性</em></label>
                        </li><li>
                        <label for="Xl_"><em>学历</em></label>
                        <div>
                        <select id="Xl_" name="Xl_"> <option value="小学" >小学</option> <option value="初中" selected>初中</option> <option value="高中" >高中</option> </select>
                        </div>
                        </li></ul></form>
                                                                        """
                        .trim(),
                form.toString());
    }

}
