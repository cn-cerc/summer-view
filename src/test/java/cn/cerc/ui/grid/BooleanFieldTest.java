package cn.cerc.ui.grid;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.fields.BooleanField;

public class BooleanFieldTest {
    @Test
    public void test() {
        DataSet dataSet = new DataSet();
        dataSet.append().setValue("allow_pass", "1");
        dataSet.append().setValue("allow_pass", "0");
        dataSet.append().setValue("allow_pass", "1");
        dataSet.append().setValue("allow_pass", "0");
        DataGrid grid = new DataGrid(null);
        grid.setDataSet(dataSet);
        new BooleanField(grid, "是否允许通过", "allow_pass", 4);
        assertEquals("""
                <div id='grid' class='scrollArea'><script>$(function() { initGrid() });</script>
                <table class="dbgrid" role="default">
                <tr>
                <th width="100.000000%" title="是否允许通过"onclick="gridSort(this,'allow_pass')"><div>是否允许通过</div></th>
                </tr>
                <tr id='tr1'>
                <td align="center" role="allow_pass"><span>
                是</span>
                </td>
                </tr>
                <tr id='tr2'>
                <td align="center" role="allow_pass"><span>
                否</span>
                </td>
                </tr>
                <tr id='tr3'>
                <td align="center" role="allow_pass"><span>
                是</span>
                </td>
                </tr>
                <tr id='tr4'>
                <td align="center" role="allow_pass"><span>
                否</span>
                </td>
                </tr>
                </table>
                </div>""", grid.toString());
    }

    @Test
    public void test2() {
        DataSet dataSet = new DataSet();
        dataSet.append().setValue("allow_pass", "1");
        dataSet.append().setValue("allow_pass", "0");
        dataSet.append().setValue("allow_pass", "1");
        dataSet.append().setValue("allow_pass", "0");
        DataGrid grid = new DataGrid(null);
        grid.setDataSet(dataSet);
        new BooleanField(grid, "是否允许通过", "allow_pass", 4).setReadonly(false);
        assertEquals(
                """
                        <div id='grid' class='scrollArea'><script>$(function() { initGrid() });</script>
                        <table class="dbgrid" role="default">
                        <tr>
                        <th width="100.000000%" title="是否允许通过"onclick="gridSort(this,'allow_pass')"><div>是否允许通过</div></th>
                        </tr>
                        <tr id='tr1'>
                        <td align="center" role="allow_pass"><span>
                        <input id='allow_pass' type='checkbox' name='allow_pass' value='true' autocomplete='off' data-allow_pass='[1]' checked onclick='tableOnChanged(this)'/>
                        </span>
                        </td>
                        </tr>
                        <tr id='tr2'>
                        <td align="center" role="allow_pass"><span>
                        <input id='allow_pass' type='checkbox' name='allow_pass' value='true' autocomplete='off' data-allow_pass='[0]' onclick='tableOnChanged(this)'/>
                        </span>
                        </td>
                        </tr>
                        <tr id='tr3'>
                        <td align="center" role="allow_pass"><span>
                        <input id='allow_pass' type='checkbox' name='allow_pass' value='true' autocomplete='off' data-allow_pass='[1]' checked onclick='tableOnChanged(this)'/>
                        </span>
                        </td>
                        </tr>
                        <tr id='tr4'>
                        <td align="center" role="allow_pass"><span>
                        <input id='allow_pass' type='checkbox' name='allow_pass' value='true' autocomplete='off' data-allow_pass='[0]' onclick='tableOnChanged(this)'/>
                        </span>
                        </td>
                        </tr>
                        </table>
                        </div>""",
                grid.toString());
    }

    @Test
    public void test3() {
        DataSet dataSet = new DataSet();
        dataSet.append().setValue("allow_pass", "1");
        dataSet.append().setValue("allow_pass", "0");
        dataSet.append().setValue("allow_pass", "1");
        dataSet.append().setValue("allow_pass", "0");
        DataGrid grid = new DataGrid(null);
        grid.setDataSet(dataSet);
        BooleanField field1 = new BooleanField(grid, "是否允许通过", "allow_pass", 4);
        field1.allowCheckedAll(true).setReadonly(false);
        assertEquals(
                """
                        <div id='grid' class='scrollArea'><script>$(function() { initGrid() });</script>
                        <table class="dbgrid" role="default">
                        <tr>
                        <th width="100.000000%" title="是否允许通过"onclick="gridSort(this,'allow_pass')"><div><input type='checkbox' data-field='allow_pass' onchange='handleGridSelectAll(this)' /></div></th>
                        </tr>
                        <tr id='tr1'>
                        <td align="center" role="allow_pass"><span>
                        <input id='allow_pass' type='checkbox' name='allow_pass' value='true' autocomplete='off' data-allow_pass='[1]' checked onclick='tableOnChanged(this)'/>
                        </span>
                        </td>
                        </tr>
                        <tr id='tr2'>
                        <td align="center" role="allow_pass"><span>
                        <input id='allow_pass' type='checkbox' name='allow_pass' value='true' autocomplete='off' data-allow_pass='[0]' onclick='tableOnChanged(this)'/>
                        </span>
                        </td>
                        </tr>
                        <tr id='tr3'>
                        <td align="center" role="allow_pass"><span>
                        <input id='allow_pass' type='checkbox' name='allow_pass' value='true' autocomplete='off' data-allow_pass='[1]' checked onclick='tableOnChanged(this)'/>
                        </span>
                        </td>
                        </tr>
                        <tr id='tr4'>
                        <td align="center" role="allow_pass"><span>
                        <input id='allow_pass' type='checkbox' name='allow_pass' value='true' autocomplete='off' data-allow_pass='[0]' onclick='tableOnChanged(this)'/>
                        </span>
                        </td>
                        </tr>
                        </table>
                        </div>""",
                grid.toString());
    }

    @Test
    public void test4() {
        DataSet dataSet = new DataSet();
        dataSet.append().setValue("allow_pass", "1").setValue("code_", "001");
        dataSet.append().setValue("allow_pass", "0").setValue("code_", "002");
        dataSet.append().setValue("allow_pass", "1").setValue("code_", "003");
        dataSet.append().setValue("allow_pass", "0").setValue("code_", "004");
        DataGrid grid = new DataGrid(null);
        grid.setDataSet(dataSet);
        BooleanField field1 = new BooleanField(grid, "是否允许通过", "allow_pass", 4);
        field1.allowCheckedAll(true).setReadonly(false);
        field1.setCustomValue(() -> dataSet.getString("code_"));
        assertEquals(
                """
                        <div id='grid' class='scrollArea'><script>$(function() { initGrid() });</script>
                        <table class="dbgrid" role="default">
                        <tr>
                        <th width="100.000000%" title="是否允许通过"onclick="gridSort(this,'allow_pass')"><div><input type='checkbox' data-field='allow_pass' onchange='handleGridSelectAll(this)' /></div></th>
                        </tr>
                        <tr id='tr1'>
                        <td align="center" role="allow_pass"><span>
                        <input id='allow_pass' type='checkbox' name='allow_pass' value='001' autocomplete='off' data-allow_pass='[1]' checked onclick='tableOnChanged(this)'/>
                        </span>
                        </td>
                        </tr>
                        <tr id='tr2'>
                        <td align="center" role="allow_pass"><span>
                        <input id='allow_pass' type='checkbox' name='allow_pass' value='002' autocomplete='off' data-allow_pass='[0]' onclick='tableOnChanged(this)'/>
                        </span>
                        </td>
                        </tr>
                        <tr id='tr3'>
                        <td align="center" role="allow_pass"><span>
                        <input id='allow_pass' type='checkbox' name='allow_pass' value='003' autocomplete='off' data-allow_pass='[1]' checked onclick='tableOnChanged(this)'/>
                        </span>
                        </td>
                        </tr>
                        <tr id='tr4'>
                        <td align="center" role="allow_pass"><span>
                        <input id='allow_pass' type='checkbox' name='allow_pass' value='004' autocomplete='off' data-allow_pass='[0]' onclick='tableOnChanged(this)'/>
                        </span>
                        </td>
                        </tr>
                        </table>
                        </div>""",
                grid.toString());
    }
}
