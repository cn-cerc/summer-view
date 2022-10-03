package cn.cerc.ui.phone;

import cn.cerc.ui.core.UIComponent;

public class UIPanelLine extends UIBlockLine {

    public UIPanelLine(UIComponent owner) {
        super(owner);
    }

    @Override
    public UIPanelLine addCell(String... fieldList) {
        super.addCell(fieldList);
        return this;
    }

    @Override
    public UIPanelCell newCell(String fieldCode) {
        return new UIPanelCell(this).setFieldCode(fieldCode);
    }

}
