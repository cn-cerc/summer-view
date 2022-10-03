package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.grid.UIDataStyle;
import cn.cerc.ui.grid.UIDataStyleImpl;
import cn.cerc.ui.vcl.UIUrl;

public class UIPhoneView extends UIAbstractView {

    public UIPhoneView(UIComponent owner) {
        super(owner);
        this.setCssClass("phone-view");
        this.setActive(this.isPhone());
    }

    @Override
    public UIPhoneView setDataSet(DataSet dataSet) {
        super.setDataSet(dataSet);
        return this;
    }

    @Override
    public UIPhoneView setDataStyle(UIDataStyleImpl dataStyle) {
        super.setDataStyle(dataStyle);
        return this;
    }

    @Override
    public UIPhoneLine addLine() {
        return new UIPhoneLine(this.block());
    }

    /**
     * 增加行数据处理器UIPhoneGridLine，此处理器以table的形式呈现
     * 
     * @return 返回创建的 UIPhoneGridLine
     */
    public UIPhoneGridLine addGrid() {
        return new UIPhoneGridLine(this.block());
    }

    /**
     * 定义的列表可为字段的总个数，或为字段总个数的2倍，后者表示字段名称要独立一列
     * 
     * @param fieldListWidth 字段列表的宽度
     * @return 返回创建的 UIPhoneGridLine
     */
    public UIPhoneGridLine addGrid(int... fieldListWidth) {
        return new UIPhoneGridLine(this.block()).split(fieldListWidth);
    }

    public static void main(String[] args) {
        var ds = new DataSet();
        ds.append().setValue("code", 1).setValue("name", "a");
        ds.append().setValue("code", 2).setValue("name", "b");
        ds.fields().get("code").setName("代码");
        ds.fields().get("name").setName("名称");
        UIPhoneView view = new UIPhoneView(null).setDataSet(ds);
        view.setDataStyle(new UIDataStyle());
        view.setBlock(new UIUrl().setHref("www.baidu.com"));
        new UIUrl(view.addLine()).setText("hello");
        view.addLine().addCell("code", "name");
        view.addGrid(2, 3).addCell("code", "name");
//        view.addGrid(2, 3, 2, 3).addCell("code", "name");
        view.setActive(true);
        System.out.println(view.toString());
    }

}
