package cn.cerc.ui.grid;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class CustomHeadGrid extends DataGrid {
    private UIComponent customHead;

    public CustomHeadGrid(UIComponent owner) {
        super(owner);
        this.setCSSClass("dbgrid customHeadGrid");
    }

    @Override
    public void outputHead(HtmlWriter html) {
        if (customHead != null)
            customHead.output(html);
    }

    public UIComponent getCustomHead() {
        return customHead;
    }

    public void setCustomHead(UIComponent customHead) {
        this.customHead = customHead;
    }

}
