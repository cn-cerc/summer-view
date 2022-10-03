package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.grid.UIDataStyle;

public class UIPanelView extends UIAbstractView {

    public UIPanelView(UIComponent owner) {
        super(owner);
        this.setCssClass("panel-view");
        this.setActive(true);
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
        UIAbstractView view = new UIPanelView(null).setDataSet(ds);
        view.setDataStyle(new UIDataStyle());
//        view.setBlock(new UIUrl().setHref("www.baidu.com"));
//        new UIUrl(view.addLine()).setText("hello");
        view.addLine().addCell("code", "name");
        view.setActive(true);
        System.out.println(view.toString());
    }
}
