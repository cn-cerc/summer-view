package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;
import cn.cerc.ui.page.StaticFile;

public class FormDateStyle extends FormStringStyle {
    public FormDateStyle(String code, DataRow dataRow) {
        super(code, dataRow);
        this.setDescriptionIcon(StaticFile.getImage("images/icon/date.png"));
        this.setClickDialog("showDateDialog", code);
    }

    public FormDateStyle(String name, String code, DataRow dataRow) {
        super(name, code, dataRow);
        this.setDescriptionIcon(StaticFile.getImage("images/icon/date.png"));
        this.setClickDialog("showDateDialog", code);
    }

    public FormDateStyle(String name, String code, int width, DataRow dataRow) {
        super(name, code, width, dataRow);
        this.setDescriptionIcon(StaticFile.getImage("images/icon/date.png"));
        this.setClickDialog("showDateDialog", code);
    }
}
