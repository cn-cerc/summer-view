package cn.cerc.ui.phone;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.DataRow;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UIDataViewImpl;
import cn.cerc.ui.grid.UIDataStyle;
import cn.cerc.ui.grid.UIDataStyleImpl;

public class UIPanelView extends UIComponent implements UIDataViewImpl {

    private boolean active;
    private UIDataStyleImpl dataStyle;
    private UIComponent block;
    private DataRow dataRow;

    public UIPanelView(UIComponent owner) {
        super(owner);
        this.setRootLabel("li");
        this.setActive(true);
    }

    @Override
    public boolean active() {
        return active;
    }

    public UIPanelView setActive(boolean active) {
        this.active = active;
        return this;
    }

    /**
     * 设置视图管理器的视图处理器
     * 
     * @param dataStyle 视图管理器
     * @return 返回视图管理器自身
     */
    public UIPanelView setDataStyle(UIDataStyleImpl dataStyle) {
        if (dataStyle != null && this.dataRow == null)
            setDataRow(dataStyle.current());
        this.dataStyle = dataStyle;
        return this;
    }

    @Override
    public UIDataStyleImpl dataStyle() {
        return dataStyle;
    }

    public UIPanelView setDataRow(DataRow dataRow) {
        this.dataRow = dataRow;
        return this;
    }

    @Override
    public DataRow dataRow() {
        return this.dataRow;
    }

    public UIComponent block() {
        if (block == null)
            block = new UIComponent(this);
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
            block.setOwner(this);
        }
        this.block = block;
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
        var dataSet = current().dataSet();
        if (dataSet != null)
            this.setCssProperty("data-row", "" + (dataSet.recNo() - 1));
        this.beginOutput(html);
        for (UIComponent item : this.getComponents()) {
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
        this.endOutput(html);
    }

    /**
     * 增加行数据处理器UIPanelView，此处理器以span的形式呈现
     * 
     * @return 返回创建的 UIPanelView
     */
    public UIPanelLine addLine() {
        return new UIPanelLine(this.block());
    }

    public static void main(String[] args) {
        var ds = new DataRow();
        ds.setValue("code", 1).setValue("name", "a");
        ds.fields().get("code").setName("代码");
        ds.fields().get("name").setName("名称");

        UIDataStyle style = new UIDataStyle(true).setDataRow(ds);
        style.addField("code").setDialog("selectCode");
        style.addField("name").setStarFlag(true).setReadonly(true);
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
