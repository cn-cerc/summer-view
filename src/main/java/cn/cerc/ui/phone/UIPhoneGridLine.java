package cn.cerc.ui.phone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.phone.UIPhoneGridCell.CellTypeEnum;
import cn.cerc.ui.vcl.UITr;

public class UIPhoneGridLine extends UIPhoneLine {
    private static final Logger log = LoggerFactory.getLogger(UIPhoneGridLine.class);
    private UITr tr = new UITr(this);
    private String[] width;

    public UIPhoneGridLine(UIComponent owner) {
        super(owner);
        this.setRootLabel("table");
    }

    public void split(String... width) {
        this.setRootLabel(width != null ? "table" : null);
        this.width = width;
    }

    @Override
    public void output(HtmlWriter html) {
        // 指定宽度输出
        boolean titleSplit = this.width.length == tr.getChildCount() * 2;
        if (!(this.width.length == tr.getChildCount() || titleSplit)) {
            log.error("split error, width size <> component count");
            return;
        }
        this.beginOutput(html);
        tr.beginOutput(html);
        // 输出每一列
        for (int i = 0; i < this.width.length; i++) {
            UIComponent item = titleSplit ? tr.getChild(i / 2) : tr.getChild(i);
            boolean isTitle = titleSplit && i % 2 == 0;
            if (titleSplit && item instanceof UIPhoneGridCell column)
                column.setCellType(isTitle ? CellTypeEnum.OnlyTitle : CellTypeEnum.OnlyValue);
            html.print(String.format("<td width='%s'>", this.width[i]));
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

    public UIPhoneGridCell addCell(String fieldCode) {
        return new UIPhoneGridCell(tr).setFieldCode(fieldCode);
    }

}
