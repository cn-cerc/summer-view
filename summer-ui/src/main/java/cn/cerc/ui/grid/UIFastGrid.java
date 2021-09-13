package cn.cerc.ui.grid;

import cn.cerc.core.DataSet;
import cn.cerc.db.editor.EditorFactory;
import cn.cerc.ui.core.UIComponent;

public class UIFastGrid extends UIComponent {

    public UIFastGrid(UIComponent owner) {
        super(owner);
        this.setRootLabel(isPhone() ? "div" : "table");
    }

    public static void main(String[] args) {
        DataSet ds = new DataSet();
        ds.append();
        ds.setField("code", "a01");
        ds.setField("name", "jason");
        ds.setField("sex", true);
        ds.append();
        ds.setField("code", "a02");
        ds.setField("name", "bade");
        ds.setField("sex", false);

        ds.getFieldDefs().get("code").setName("工号");
        ds.getFieldDefs().get("name").setName("姓名");
        ds.getFieldDefs().get("sex").setName("性别").onGetSetText(EditorFactory.ofBoolean("女的", "男的"));

        UIFastGrid grid = new UIFastGrid(null);
        grid.setPhone(false);
        if (grid.isPhone()) {
            new UIGridBody(grid, ds).addAll(ds.getFieldDefs());
        } else {
            UIGridHead head = new UIGridHead(grid);
            UIGridBody body = new UIGridBody(grid, ds).addAll(ds.getFieldDefs());
            head.addAll(body.getColumns());
        }
        System.out.println(grid.toString());
    }

}
