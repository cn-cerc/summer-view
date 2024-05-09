package cn.cerc.ui.grid;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataRow;
import cn.cerc.ui.phone.UIPanelView;
import cn.cerc.ui.vcl.UIButton;
import cn.cerc.ui.vcl.UIForm;

public class UIDataStyleTest {

    @Test
    public void test1() {
        UIDataStyle style = new UIDataStyle();
        DataRow dataRow = new DataRow();
        dataRow.setValue("price_", "100");
        style.setDataRow(dataRow);
        style.addField("price_").setReadonly(false).onGetText(style.getDouble());
        UIGridView grid = new UIGridView(null);
        grid.setDataStyle(style);
        assertEquals(grid.toString(),
                """
                        <script>$(function() { initGrid() });</script>
                        <table class='dbgrid gridView'><tr><th onclick='gridViewSort(this)'><div>序</div></th><th><div>price_</div></th></tr><tr><td align='center'>1</td><td><input autocomplete='off' name='price_' id='price_' type='number' value='100'></input></td></tr>
                        </table>"""
                        .replaceAll("\n", System.lineSeparator()));
    }

    @Test
    public void test2() {
        UIDataStyle style = new UIDataStyle(false);
        DataRow dataRow = new DataRow();
        dataRow.setValue("price_", "100");
        style.setDataRow(dataRow);
        style.addField("price_").setName("价格").onGetText(style.getDouble());
        UIForm form = new UIForm(null);
        UIPanelView find = new UIPanelView(form).setDataStyle(style);
        var line = find.addLine();
        for (var item : style.fields().keySet())
            line.addCell(item);
        new UIButton(form).setText("查询").setId("submit");
        assertEquals(form.toString(),
                """
                        <form method='post'><div data-line='0'><span data-field='price_'>价格:<input autocomplete='off' name='price_' id='price_' type='number' value='100'></input></span></div><button id='submit' name="submit">查询</button>
                        </form>"""
                        .replaceAll("\n", System.lineSeparator()));
    }
}
