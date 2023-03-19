package cn.cerc.ui.core;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataRowSource;

public interface SearchSource extends DataRowSource {

    void updateValue(String id, String code);

    default DataRow current() {
        return currentRow().orElse(null);
    }

}
