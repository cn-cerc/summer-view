package cn.cerc.ui.phone;

import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UIDataViewImpl;
import cn.cerc.ui.phone.UIBlockGridCell.CellTypeEnum;
import cn.cerc.ui.vcl.UITr;

public class UIBlockGridLine extends UIBlockLine {
    private static final Logger log = LoggerFactory.getLogger(UIBlockGridLine.class);
    private UITr tr = new UITr(this);
    private String[] width;

    public UIBlockGridLine(UIComponent owner) {
        super(owner);
        this.setRootLabel("table");
    }

    public UIBlockGridLine split(String... width) {
        this.width = width;
        return this;
    }

    public UIBlockGridLine split(int... width) {
        int total = 0;
        for (int item : width)
            total += item;
        if (total <= 0)
            throw new RuntimeException("validate width fail");

        this.width = new String[width.length];
        var fmt = new DecimalFormat("0");
        for (int i = 0; i < width.length; i++)
            this.width[i] = String.format("%s%%", fmt.format((float) width[i] * 100 / total));
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.width == null) {
            int[] items = new int[this.getComponentCount() * 2];
            for (int i = 0; i < items.length; i++)
                items[i] = 1;
            this.split(items);
        }
        // 指定宽度输出
        boolean titleSplit = this.width.length == tr.getComponentCount() * 2;
        if (!(this.width.length == tr.getComponentCount() || titleSplit)) {
            log.error("split error, width size <> component count");
            return;
        }
        this.beginOutput(html);
        tr.beginOutput(html);
        // 输出每一列
        for (int i = 0; i < this.width.length; i++) {
            UIComponent item = titleSplit ? tr.getComponent(i / 2) : tr.getComponent(i);
            boolean isTitle = titleSplit && i % 2 == 0;
            if (titleSplit) {
                if (item instanceof UIBlockGridCell column)
                    column.setCellType(isTitle ? CellTypeEnum.OnlyTitle : CellTypeEnum.OnlyValue);
                else
                    log.warn("item 不支持的类型：" + item.getClass().getName());
            }
            html.print("<td");
            if (!Utils.isEmpty(this.width[i]))
                html.print(String.format(" width='%s'", this.width[i]));
            html.print(">");
            item.setRootLabel(null);
            item.output(html);
            html.print("</td>");
        }
        tr.endOutput(html);
        this.endOutput(html);
    }

    public UITr tr() {
        return tr;
    }

    public UIBlockGridLine addCell(String... fieldList) {
        var impl = findOwner(UIDataViewImpl.class);
        if (impl == null) {
            log.error("在 owner 中找不到 UIDataViewImpl");
            throw new RuntimeException("在 owner 中找不到 UIDataViewImpl");
        }
        var fields = impl.dataSet().fields();
        var defaultStyle = impl.active() ? impl.defaultStyle() : null;
        for (var fieldCode : fieldList) {
            FieldMeta column = fields.get(fieldCode);
            if (column == null)
                column = fields.add(fieldCode, FieldKind.Calculated);
            if (defaultStyle != null)
                defaultStyle.setDefault(column);
            new UIBlockGridCell(tr).setFieldCode(fieldCode);
        }
        return this;
    }

    public UIBlockGridCell getCell(int index) {
        return (UIBlockGridCell) tr.getComponent(index);
    }

}
