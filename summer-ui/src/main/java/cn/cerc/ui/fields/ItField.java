package cn.cerc.ui.fields;

import cn.cerc.core.ClassResource;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class ItField extends AbstractField {

    private static final ClassResource res = new ClassResource(ItField.class, SummerUI.ID);

    public ItField(UIComponent owner) {
        super(owner, res.getString(1, "序"), "_it_", 2);
        this.setReadonly(true);
        this.setShortName("");
        this.setAlign("center");
    }

    @Override
    public String getText() {
        if (getBuildText() != null) {
            HtmlWriter html = new HtmlWriter();
            getBuildText().outputText(getCurrent(), html);
            return html.toString();
        }
        return "" + getCurrent().getDataSet().getRecNo();
    }

    @Override
    public String getField() {
        return "_it_";
    }

    @Override
    public Title createTitle() {
        Title title = super.createTitle();
        title.setType("int");
        return title;
    }

    @Override
    public ItField setReadonly(boolean readonly) {
        super.setReadonly(true);
        return this;
    }

}
