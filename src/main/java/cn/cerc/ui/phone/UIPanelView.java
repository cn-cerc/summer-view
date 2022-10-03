package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.grid.UIDataStyle;
import cn.cerc.ui.grid.UIDataStyleImpl;

public class UIPanelView extends UIAbstractView {

    public UIPanelView(UIComponent owner) {
        super(owner);
        this.setCssClass("panel-view");
        this.setActive(true);
    }

    @Override
    public UIPanelView setDataSet(DataSet dataSet) {
        super.setDataSet(dataSet);
        return this;
    }

    @Override
    public UIPanelView setDataStyle(UIDataStyleImpl dataStyle) {
        super.setDataStyle(dataStyle);
        return this;
    }

    @Override
    public UIPanelLine addLine() {
        return new UIPanelLine(this.block());
    }

    public static void main(String[] args) {
        var ds = new DataSet();
        ds.append().setValue("code", 1).setValue("name", "a");
        ds.append().setValue("code", 2).setValue("name", "b");
        ds.fields().get("code").setName("代码");
        ds.fields().get("name").setName("名称");

        var style = new UIDataStyle(true).setDataSet(ds);
        style.addField("code").setDialog("selectCode");
        style.addField("name").setStarFlag(true);
        UIPanelView view = new UIPanelView(null).setDataStyle(style);

        UIPanelLine line = view.addLine().onCreateCellAfter((owner, field) -> {
//            if (style.inputState())
//                new UISelectDialog(owner).setInputId(field.code()).setDialog("selectCode");
        });
        line.addCell("code", "name");
        view.setActive(true);
        System.out.println(view.toString());
    }
}
