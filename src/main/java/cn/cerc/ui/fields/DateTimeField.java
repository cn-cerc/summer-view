package cn.cerc.ui.fields;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.UIComponent;

public class DateTimeField extends AbstractField {
    private static final ClassConfig config = new ClassConfig(DateField.class, SummerUI.ID);

    public DateTimeField(UIComponent owner, String name, String field) {
        super(owner, name, field, 10);
        this.setDialog("showDateTimeDialog");
        this.setIcon(config.getClassProperty("icon", ""));
        this.setAlign("center");
    }

    public DateTimeField(UIComponent owner, String name, String field, int width) {
        super(owner, name, field, width);
        this.setDialog("showDateTimeDialog");
        this.setIcon(config.getClassProperty("icon", ""));
        this.setAlign("center");
    }

    @Override
    public Title createTitle() {
        Title title = super.createTitle();
        title.setType("date");
        return title;
    }

    @Override
    public String getText() {
        if (getBuildText() != null) {
            HtmlWriter html = new HtmlWriter();
            getBuildText().outputText(current(), html);
            return html.toString();
        }
        if (current().has(getField())) {
            return current().getDatetime(getField()).toString();
        } else {
            return "";
        }
    }

}
