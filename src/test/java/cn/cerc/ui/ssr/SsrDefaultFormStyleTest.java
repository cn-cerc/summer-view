package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataRow;
import cn.cerc.ui.ssr.form.VuiForm;

public class SsrDefaultFormStyleTest {

    @Test
    public void test() {
        var form = new VuiForm(null);
        form.strict(false);
        form.dataRow(DataRow.of("Name_", "jason", "Man_", true, "Xl_", "初中"));

        var style = form.defaultStyle();
        form.addBlock(style.getString("姓名", "Name_"));
        form.addBlock(style.getBoolean("男性", "Man_"));
        var ssr = form.addBlock(style.getMap("学历", "Xl_"));
        ssr.toMap("小学", "小学");
        ssr.toMap("初中", "初中");
        ssr.toMap("高中", "高中");
        form.addColumn("姓名", "男性", "学历");

        assertEquals(
                """
                        <form method='post' action='' id='form1' role='search'><ul><li>
                        <label for="Name_"><em>姓名</em></label>
                        <div>
                        <input type="text" name="Name_" id="Name_" value="jason" autocomplete="off" placeholder="请输入姓名"/>
                        <span role="suffix-icon"></span>
                        </div>
                        </li><li>
                        <div role="switch">
                        <input autocomplete="off" name="Man_" id="Man_" type="checkbox" value="1" checked  />
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
