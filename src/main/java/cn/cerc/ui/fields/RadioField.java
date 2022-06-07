package cn.cerc.ui.fields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cn.cerc.ui.core.UIComponent;

public class RadioField extends AbstractField {

    private final List<String> items = new ArrayList<>();

    public RadioField(UIComponent owner, String name, String field, int width) {
        super(owner, name, field, width);
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
