package cn.cerc.ui.phone;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIPhoneLine extends UIComponent {
    private int[] width;

    public UIPhoneLine(UIComponent owner) {
        super(owner);
        this.setRootLabel("div");
    }

    public void split(int... width) {
        this.setRootLabel("table");
        this.width = width;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.width == null) {
            super.output(html);
            return;
        }
        // 指定宽度输出
        if (this.width.length != this.getComponentCount())
            html.print("split error, width size <> component count");
        this.beginOutput(html);
        html.print("<tr>");
        int i = 0;
        for (var item : this.getComponents()) {
            html.print(String.format("<td width='%d'>", this.width[i++]));
            item.output(html);
            html.print("</td>");
        }
        html.print("</tr>");
        this.endOutput(html);
    }

}
