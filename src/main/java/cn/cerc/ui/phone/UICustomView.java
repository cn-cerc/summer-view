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

public class UICustomView extends UIComponent implements UIDataViewImpl {

    private boolean active;
    private UIDataStyleImpl dataStyle;
    private DataSet dataSet;
    private UIComponent block;
    private UILi li;

    public UICustomView(UIComponent owner) {
        super(owner);
        this.setRootLabel("ol");
        this.setCssClass("panel-view");
        this.setActive(true);
    }

    @Override
    public boolean active() {
        return active;
    }

    public UICustomView setActive(boolean active) {
        this.active = active;
        return this;
    }

    /**
     * 设置视图管理器的视图处理器
     * 
     * @param dataStyle 视图管理器
     * @return 返回视图管理器自身
     */
    public UICustomView setDataStyle(UIDataStyleImpl dataStyle) {
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

    public UICustomView setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    /**
     * 增加行数据处理器UIPanelView，此处理器以span的形式呈现
     * 
     * @return 返回创建的 UIPanelView
     */
    public UIPhoneLine addLine() {
        return new UIPhoneLine(this.block());
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
     */
    public void setBlock(UIComponent block) {
        if (block != null) {
            if (this.block != null)
                throw new RuntimeException("block not is null");
            block.setOwner(li());
        }
        this.block = block;
    }

    public UIComponent li() {
        if (li == null)
            li = new UILi(this);
        return li;
    }

    public List<UIPhoneLine> lines() {
        List<UIPhoneLine> lines = new ArrayList<>();
        for (var item : this.block().getComponents()) {
            if (item instanceof UIPhoneLine line)
                lines.add(line);
        }
        return lines;
    }

    public UIPhoneLine getLine(int index) {
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
    
    public static void main(String[] args) {
        var ds = new DataSet();
        ds.append().setValue("code", 1).setValue("name", "a");
        ds.append().setValue("code", 2).setValue("name", "b");
        ds.fields().get("code").setName("代码");
        ds.fields().get("name").setName("名称");
        UICustomView view = new UICustomView(null).setDataSet(ds);
        view.setDataStyle(new UIDataStyle());
        view.setBlock(new UIUrl().setHref("www.baidu.com"));
        new UIUrl(view.addLine()).setText("hello");
        view.addLine().addCell("code", "name");
//        view.addGrid(2, 3, 2, 3).addCell("code", "name");
        view.setActive(true);
        System.out.println(view.toString());
    }
}
