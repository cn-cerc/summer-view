package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.grid.UIDataStyle;
import cn.cerc.ui.vcl.UISpan;

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
    public UIPanelLine addLine() {
        return new UIPanelLine(this.block());
    }

    public static void main(String[] args) {
        var ds = new DataSet();
        ds.append().setValue("code", 1).setValue("name", "a");
        ds.append().setValue("code", 2).setValue("name", "b");
        ds.fields().get("code").setName("代码");
        ds.fields().get("name").setName("名称");
        UIPanelView view = new UIPanelView(null).setDataSet(ds);
        view.setDataStyle(new UIDataStyle());
        UIPanelLine line = view.addLine().onCreateCell((owner, field) -> {
            new UISpan(owner).setText("js:dialog:" + field.code());
        });
        line.addCell("code", "name");
        view.setActive(true);
        System.out.println(view.toString());
    }
}
