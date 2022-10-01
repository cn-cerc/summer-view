package cn.cerc.ui.phone;

import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.editor.OnGetText;
import cn.cerc.ui.grid.UIViewStyleImpl;

public class UIPhoneStyle implements UIViewStyleImpl {

    @Override
    public OnGetText getDefault(FieldMeta fieldMeta) {
        return this.getString();
    }

    public OnGetText getString() {
        return data -> {
            FieldMeta meta = data.source().fields().get(data.key());
            return String.format("%s: %s", meta.name(), data.getString());
        };
    }

}
