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
import cn.cerc.ui.grid.UIViewStyleImpl;
import cn.cerc.ui.vcl.UIUrl;

public class UIPhoneView extends UIComponent implements DataSource {
    private UIViewStyleImpl defaultStyle;
    private UIComponent block;
    private DataSet dataSet;

    public UIPhoneView(UIComponent owner) {
        super(owner);
        this.setRootLabel("li");
    }

    public UIPhoneView setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public UIPhoneLine addLine() {
        return new UIPhoneLine(this.block());
    }

    public UIPhoneLine addLine(String... fields) {
        UIPhoneLine line = addLine();
        for (var fieldCode : fields) {
            FieldMeta column = dataSet.fields().get(fieldCode);
            if (column == null)
                column = dataSet.fields().add(fieldCode, FieldKind.Calculated);
            if (defaultStyle != null)
                column.onGetText(defaultStyle.getDefault(column));
            line.addColumn(fieldCode);
        }
        return line;
    }

    public UIPhoneGridLine addGrid(String... fields) {
        UIPhoneGridLine line = new UIPhoneGridLine(this.block());
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

    public UIPhoneView setDefaultStyle(UIViewStyleImpl defaultStyle) {
        this.defaultStyle = defaultStyle;
        return this;
    }

    public UIViewStyleImpl defaultStyle() {
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
        if (!this.isPhone())
            return;
        html.println("<ul class='phone-view'>");
        dataSet.first();
        while (dataSet.fetch()) {
            this.setProperty("data-row", "" + (dataSet.recNo() - 1));
            this.beginOutput(html);
            html.println("");
            for (UIComponent item : this.children()) {
                item.beginOutput(html);
                var line = 0;
                for (UIComponent child : item.children()) {
                    child.setProperty("data-line", "" + (line++));
                    child.output(html);
                    if (line < item.getChildCount())
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

    public List<UIPhoneLine> lines() {
        List<UIPhoneLine> lines = new ArrayList<>();
        for (var item : this.block().children()) {
            if (item instanceof UIPhoneLine line)
                lines.add(line);
        }
        return lines;
    }

    public UIPhoneLine getLine(int index) {
        return lines().get(index);
    }

    public static void main(String[] args) {
        var ds = new DataSet();
        ds.append().setValue("code", 1).setValue("name", "a");
        ds.append().setValue("code", 2).setValue("name", "b");
        ds.fields().get("code").setName("代码");
        ds.fields().get("name").setName("名称");
        UIPhoneView view = new UIPhoneView(null).setDataSet(ds);
        view.setDefaultStyle(new UIPhoneStyle());
        view.setBlock(new UIUrl().setHref("www.baidu.com"));
        new UIUrl(view.addLine()).setText("hello");
        view.addLine("code", "name");
        view.addGrid("code", "name").split(2, 3);
        view.addGrid("code", "name").split(2, 3, 2, 3);
        view.setPhone(true);
        System.out.println(view.toString());
    }

}
