package cn.cerc.ui.core;

import cn.cerc.core.DataSource;

public interface SearchSource extends DataSource {

    void updateValue(String id, String code);
    
}
