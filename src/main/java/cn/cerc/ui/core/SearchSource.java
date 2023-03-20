package cn.cerc.ui.core;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSetSource;

public interface SearchSource extends DataSetSource {

    void updateValue(String id, String code);

    default DataRow current() {
        return this.getDataSet().get().current();
    }

}
