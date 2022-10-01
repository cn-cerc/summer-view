package cn.cerc.ui.grid;

import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.editor.OnGetText;

public interface UIViewStyleImpl {

    OnGetText getDefault(FieldMeta fieldMeta);

}
