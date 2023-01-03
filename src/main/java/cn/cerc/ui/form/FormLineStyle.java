package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;

public class FormLineStyle {
    // 栅格布局外部宽度，默认为12（整行）
    private int outWidth = 12;
    private DataRow dataRow;
    private FormLineStyleImpl lineStyle;

    public FormLineStyle(FormLineStyleImpl lineStyle) {
        this.setLineStyle(lineStyle);
    }

    public FormLineStyle(FormLineStyleImpl styleImpl, DataRow dataRow) {
        this.setLineStyle(styleImpl);
        this.dataRow = dataRow;
    }

    public void setLineStyle(FormLineStyleImpl styleImpl) {
        this.lineStyle = styleImpl;
    }

}
