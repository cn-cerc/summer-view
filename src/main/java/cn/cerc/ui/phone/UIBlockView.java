package cn.cerc.ui.phone;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.DataSource;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.grid.UIOutputStyleImpl;
import cn.cerc.ui.vcl.UIUrl;

public class UIBlockView extends UIComponent implements DataSource {
    private UIOutputStyleImpl defaultStyle;
    private UIComponent block;
    private DataSet dataSet;
    private boolean active = true;

    public UIBlockView(UIComponent owner) {
        super(owner);
        this.setRootLabel("li");
    }

    public UIBlockView setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public UIBlockLine addLine() {
        return new UIBlockLine(this.block());
    }

    public UIBlockLine addLine(String... fields) {
        UIBlockLine line = addLine();
        line.addColumns(fields);
        return line;
    }

    public UIBlockGridLine addLineGrid(String... fields) {
        UIBlockGridLine line = new UIBlockGridLine(this.block());
        for (var fieldCode : fields) {
            FieldMeta column = dataSet.fields().get(fieldCode);
            if (column == null)
                column = dataSet.fields().add(fieldCode, FieldKind.Calculated);
            if (defaultStyle != null)
                column.onGetText(defaultStyle.getDefault(column));
            line.addCell(fieldCode);
        }
        return line;
    }

    public UIBlockView setDefaultStyle(UIOutputStyleImpl defaultStyle) {
        this.defaultStyle = defaultStyle;
        return this;
    }

    public UIOutputStyleImpl defaultStyle() {
        return defaultStyle;
    }

    public UIComponent block() {
        if (block == null)
            block = new UIComponent(this);
        return block;
    }

    public void setBlock(UIComponent block) {
        if (block != null) {
            if (this.block != null)
                throw new RuntimeException("block not is null");
            block.setOwner(this);
        }
        this.block = block;
    }

    @Override
    public void output(HtmlWriter html) {
        if (!this.active())
            return;
        html.println("<ul class='block-view'>");
        dataSet.first();
        while (dataSet.fetch()) {
            this.setCssProperty("data-row", "" + (dataSet.recNo() - 1));
            this.beginOutput(html);
            html.println("");
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
            html.println("");
        }
        html.print("</ul>");
    }

    @Override
    public DataRow current() {
        return dataSet != null ? dataSet.current() : new DataRow();
    }

    @Override
    public boolean isReadonly() {
        return dataSet != null ? dataSet.readonly() : true;
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

    public DataSet dataSet() {
        return dataSet;
    }

    public boolean active() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static void main(String[] args) {
        var ds = new DataSet();
        ds.append().setValue("code", 1).setValue("name", "a");
        ds.append().setValue("code", 2).setValue("name", "b");
        ds.fields().get("code").setName("代码");
        ds.fields().get("name").setName("名称");
        UIBlockView view = new UIBlockView(null).setDataSet(ds);
        view.setDefaultStyle(new UIBlockStyle());
        view.setBlock(new UIUrl().setHref("www.baidu.com"));
        new UIUrl(view.addLine()).setText("hello");
        view.addLine("code", "name");
        view.addLineGrid("code", "name").split(2, 3);
        view.addLineGrid("code", "name").split(2, 3, 2, 3);
        view.setPhone(true);
        System.out.println(view.toString());
    }

}
