package cn.cerc.ui.grid;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.editor.EditorFactory;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.style.IGridStyle;

public class UIFastGrid extends UIComponent implements IGridStyle {

    public UIFastGrid(UIComponent owner) {
        super(owner);
        this.setRootLabel(isPhone() ? "div" : "table");
    }

    public static void main(String[] args) {
        DataSet ds = new DataSet();
        ds.append();
        ds.setValue("code", "a01");
        ds.setValue("name", "jason");
        ds.setValue("sex", true);
        ds.append();
        ds.setValue("code", "a02");
        ds.setValue("name", "bade");
        ds.setValue("sex", false);

        ds.fields().get("code").setName("工号");
        ds.fields().get("name").setName("姓名");
        ds.fields().get("sex").setName("性别").onGetSetText(EditorFactory.ofBoolean("女的", "男的"));

        UIFastGrid grid = new UIFastGrid(null);
        grid.setPhone(false);
        if (grid.isPhone()) {
            new UIGridBody(grid, ds).addAll(ds.fields());
        } else {
            UIGridHead head = new UIGridHead(grid);
            UIGridBody body = new UIGridBody(grid, ds).addAll(ds.fields());
            head.addAll(body.getColumns());
        }
        System.out.println(grid.toString());
    }

}
