package cn.cerc.ui.vcl;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UITable extends UIBaseHtml {

    public UITable() {
        this(null);
    }

    public UITable(UIComponent component) {
        super(component);
    }

    @Override
    public void output(HtmlWriter html) {
        html.print("<table");
        if (this.getCssClass() != null)
            html.print(" class=\"%s\"", this.getCssClass());
        html.print(">");
        for (UIComponent item : this)
            item.output(html);
        html.print("</table>");
    }

    public static void main(String[] args) {
        UITable table = new UITable();
        UITr tr1 = new UITr(table);
        new UITh(tr1).setText("标题");;
        
        UITr tr2 = new UITr(table);
        UITd td = new UITd(tr2);
        td.setColspan(2);
        td.setText("hello");
        System.out.println(table);
    }
}
