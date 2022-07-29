package cn.cerc.ui.fields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIInput;

public class RadioField extends AbstractField {

    private final List<String> items = new ArrayList<>();

    public RadioField(UIComponent owner, String name, String field, int width) {
        super(owner, name, field, width);
    }

    public RadioField(UIComponent owner, String name, String field) {
        super(owner, name, field, 0);
    }

    @Override
    public String getText() {
        int val = current().getInt(this.getField());
        if (val < 0 || val > items.size() - 1) {
            return "" + val;
        }
        String result = items.get(val);
        if (result == null) {
            return "" + val;
        } else {
            return result;
        }
    }

    @Override
    public void output(HtmlWriter html) {
        String current = this.getText();
        if (Utils.isEmpty(current) && !Utils.isEmpty(this.getValue())) {
            current = this.getValue();
        }
        html.println("<label for=\"%s\">%s</label>", this.getId(), this.getName() + "：");
        for (int i = 0; i < items.size(); i++) {
            String value = items.get(i);
            if (UIInput.TYPE_TEXT.equals(this.getHtmType())) {
                this.setHtmType("radio");
            }
            html.print("%s:<input type='%s' value=\"%s\" id=\"%s\" name=\"%s\"", value, this.getHtmType(), i,
                    this.getId(), this.getId());
            if (current.equals(value)) {
                html.print(" checked");
            }
            html.print("/>");
        }
        if (this.isShowStar()) {
            html.print("<font>*</font>");
        }
        html.print("<span></span>");
    }

    public RadioField add(String... items) {
        this.items.addAll(Arrays.asList(items));
        return this;
    }

    public RadioField add(Collection<String> items) {
        this.items.addAll(items);
        return this;
    }

    public RadioField add(Enum<?>[] enums) {
        for (Enum<?> item : enums) {
            this.items.add(item.name());
        }
        return this;
    }

}
