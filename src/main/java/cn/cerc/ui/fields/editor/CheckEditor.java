package cn.cerc.ui.fields.editor;

import cn.cerc.db.core.DataRow;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.fields.AbstractField;

public class CheckEditor {
    private AbstractField owner;
    private String onUpdate;
    private String value = "true";

    public CheckEditor(AbstractField owner) {
        this.owner = owner;
    }

    public String getOnUpdate() {
        return onUpdate;
    }

    public CheckEditor setOnUpdate(String onUpdate) {
        this.onUpdate = onUpdate;
        return this;
    }

    public String getValue() {
        return value;
    }

    public CheckEditor setValue(String value) {
        this.value = value;
        return this;
    }

    public String format(DataRow ds) {
        String data = ds.getString(owner.getField());

        HtmlWriter html = new HtmlWriter();
        html.print("<input");
        html.print(" id='%s'", owner.getId());
        html.print(" type='checkbox'");
        html.print(" name='%s'", owner.getField());
        html.print(" value='%s'", getValue());
        html.print(" autocomplete='off'");
        html.print(" data-%s='[%s]'", owner.getField(), data);
        if (ds.getBoolean(owner.getField())) {
            html.print(" checked");
        }
        if (onUpdate != null) {
            html.print(" onclick=\"tableOnChanged(this,'%s')\"", onUpdate);
        } else {
            html.print(" onclick='tableOnChanged(this)'");
        }
        html.println("/>");
        return html.toString();
    }
}
