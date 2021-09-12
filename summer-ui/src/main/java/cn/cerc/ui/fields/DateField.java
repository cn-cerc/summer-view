package cn.cerc.ui.fields;

import cn.cerc.core.ClassConfig;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class DateField extends AbstractField {
    private static final ClassConfig config = new ClassConfig(DateField.class, SummerUI.ID);

    public DateField(UIComponent owner, String name, String field) {
        super(owner, name, field, 5);
        this.setDialog("showDateDialog");
        this.setIcon(Application.getStaticPath() + config.getClassProperty("icon", ""));
        this.setAlign("center");
    }

    public DateField(UIComponent owner, String name, String field, int width) {
        super(owner, name, field, width);
        this.setDialog("showDateDialog");
        this.setIcon(Application.getStaticPath() + config.getClassProperty("icon", ""));
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
        if (buildText != null) {
            HtmlWriter html = new HtmlWriter();
            buildText.outputText(getCurrent(), html);
            return html.toString();
        }
        if (getCurrent().hasValue(getField())) {
            return getCurrent().getDatetime(getField()).getDate();
        } else {
            return "";
        }
    }
}
