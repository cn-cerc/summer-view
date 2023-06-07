package cn.cerc.ui.fields;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.UIComponent;

public class DateField extends AbstractField {
    public static final ClassConfig config = new ClassConfig(DateField.class, SummerUI.ID);

    public DateField(UIComponent owner, String name, String field) {
        super(owner, name, field, 5);
        this.setDialog("showDateDialog");
        var impl = Application.getBean(ImageConfigImpl.class);
        if (impl != null)
            this.getDialog().setIcon(impl.getClassProperty(DateField.class, SummerUI.ID, "icon", ""));
        else
            this.getDialog().setIcon(config.getClassProperty("icon", ""));
        this.setAlign("center");
    }

    public DateField(UIComponent owner, String name, String field, int width) {
        super(owner, name, field, width);
        this.setDialog("showDateDialog");
        var impl = Application.getBean(ImageConfigImpl.class);
        if (impl != null)
            this.getDialog().setIcon(impl.getClassProperty(DateField.class, SummerUI.ID, "icon", ""));
        else
            this.getDialog().setIcon(config.getClassProperty("icon", ""));
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
        if (current() != null && current().hasValue(getField())) {
            return current().getDatetime(getField()).getDate();
        } else {
            return "";
        }
    }
}
