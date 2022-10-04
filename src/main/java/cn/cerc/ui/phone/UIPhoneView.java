package cn.cerc.ui.phone;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UIDataViewImpl;
import cn.cerc.ui.grid.UIDataStyle;
import cn.cerc.ui.grid.UIDataStyleImpl;
import cn.cerc.ui.vcl.UILi;
import cn.cerc.ui.vcl.UIUrl;

public class UIPhoneView extends UIComponent implements UIDataViewImpl {

    private boolean active;
    private UIDataStyleImpl dataStyle;
    private DataSet dataSet;
    private UIComponent block;
    private UILi li;

    public UIPhoneView(UIComponent owner) {
        super(owner);
        this.setRootLabel("ol");
        this.setCssClass("phone-view");
        this.setActive(this.isPhone());
    }

    @Override
    public boolean active() {
        return active;
    }

    public UIPhoneView setActive(boolean active) {
        this.active = active;
        return this;
    }

    /**
     * 设置视图管理器的视图处理器
     * 
     * @param dataStyle 视图管理器
     * @return 返回视图管理器自身
     */
    public UIPhoneView setDataStyle(UIDataStyleImpl dataStyle) {
        if (dataStyle != null && this.dataSet == null)
            this.dataSet = dataStyle.dataSet();
        this.dataStyle = dataStyle;
        return this;
    }

    @Override
    public UIDataStyleImpl dataStyle() {
        return dataStyle;
    }

    @Override
    public DataSet dataSet() {
        return dataSet;
    }

    public UIPhoneView setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public UIComponent block() {
        if (block == null)
            block = new UIComponent(li());
        return block;
    }

    /**
     * 若要整块数据可执行点示时，可设置此属性为 UIUrl 对象
     * 
     * @param block 设置每一条数据的包裹对象
     * @return block对象
     */
    public UIComponent setBlock(UIComponent block) {
        if (block != null) {
            if (this.block != null)
                throw new RuntimeException("block not is null");
            block.setOwner(li());
        }
        this.block = block;
        return block;
    }

    public UIComponent li() {
        if (li == null)
            li = new UILi(this);
        return li;
    }

    public List<UIBlockLine> lines() {
        List<UIBlockLine> lines = new ArrayList<>();
        for (var item : this.block().getComponents()) {
            if (item instanceof UIBlockLine line)
                lines.add(line);
        }
        return lines;
    }

    public UIBlockLine getLine(int index) {
        return lines().get(index);
    }

    @Override
    public void output(HtmlWriter html) {
        if (!this.active())
            return;
        this.beginOutput(html);
        var li = li();
        var dataSet = dataSet();
        dataSet.first();
        while (dataSet.fetch()) {
            li.setCssProperty("data-row", "" + (dataSet.recNo() - 1));
            li.beginOutput(html);
            for (UIComponent item : li.getComponents()) {
                item.beginOutput(html);
                var line = 0;
                for (UIComponent child : item.getComponents()) {
                    child.setCssProperty("data-line", "" + (line++));
                    child.output(html);
                    if (line < item.getComponentCount())
                        html.println("");
                }
                item.endOutput(html);
                html.println("");
            }
            li.endOutput(html);
            html.println("");
        }
        this.endOutput(html);
    }

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
