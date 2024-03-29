package cn.cerc.ui.phone;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UIDataViewImpl;
import cn.cerc.ui.grid.UIDataStyle;
import cn.cerc.ui.grid.UIDataStyleImpl;
import cn.cerc.ui.vcl.UIButton;
import cn.cerc.ui.vcl.UIForm;

public class UIPanelView extends UIComponent implements UIDataViewImpl {

    private boolean active;
    private UIDataStyleImpl dataStyle;
    private UIComponent block;
    private DataRow dataRow;

    public UIPanelView(UIComponent owner) {
        super(owner);
        this.setRootLabel("");
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
            dataStyle.getDataSet().ifPresent(item -> setDataRow(item.current()));
        this.dataStyle = dataStyle;
        return this;
    }

    @Override
    public UIDataStyleImpl dataStyle() {
        return dataStyle;
    }

    @Override
    public Optional<DataSet> getDataSet() {
        if (dataRow == null)
            return Optional.empty();
        else
            return Optional.ofNullable(dataRow.dataSet());
    }

    public UIPanelView setDataRow(DataRow dataRow) {
        this.dataRow = dataRow;
        return this;
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
     * @return block对象
     */
    public UIComponent setBlock(UIComponent block) {
        if (block != null) {
            if (this.block != null)
                throw new RuntimeException("block not is null");
            block.setOwner(this);
        }
        this.block = block;
        return block;
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
        var dataSet = getDataSet().orElse(null);
        if (dataSet != null)
            this.setCssProperty("data-row", "" + (dataSet.recNo() - 1));
        super.output(html);
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

        UIDataStyle style = new UIDataStyle(false).setDataRow(ds);
        style.addField("code").setDialog("selectCode");
        style.addField("name").setRequired(true).setReadonly(true);
        var form = new UIForm(null).setAction("FrmXXX");
        UIPanelView view = new UIPanelView(form).setDataStyle(style);
        view.addLine().addCell("code");
        view.addLine().addCell("name");
        new UIButton(form).setName("summit");
        System.out.println(form.gatherRequest());
        System.out.println(form.toString());
    }

}
