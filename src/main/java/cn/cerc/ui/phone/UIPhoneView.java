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
import cn.cerc.ui.grid.UIGridStyle;
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

    public UIPhoneLine addLine(String... fields) {
        UIPhoneLine line = addLine();
        for (var fieldCode : fields) {
            FieldMeta column = dataSet.fields().get(fieldCode);
            if (column == null)
                column = dataSet.fields().add(fieldCode, FieldKind.Calculated);
            if (defaultStyle != null)
                column.onGetText(defaultStyle.getDefault(column));
            new UIPhoneColumn(line).setFieldCode(fieldCode);
        }
        return line;
    }

    public UIPhoneLine addLine() {
        return new UIPhoneLine(this.block());
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
        html.println("<ul class='phone-dataSet'>");
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
        UIPhoneView view = new UIPhoneView(null);
        view.setDefaultStyle(new UIGridStyle());
        view.setDataSet(ds);
        view.setBlock(new UIUrl().setHref("baidu"));
        view.addLine("code", "name").split(50, 50);
        new UIUrl(view.addLine()).setText("hello");
        System.out.println(view.getLine(1).getChild(0).getClass());
        System.out.println(view.toString());
    }

}
