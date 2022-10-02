package cn.cerc.ui.core;

import cn.cerc.db.core.DataRowSourceImpl;

public interface SearchSource extends DataRowSourceImpl {

    void updateValue(String id, String code);

}
