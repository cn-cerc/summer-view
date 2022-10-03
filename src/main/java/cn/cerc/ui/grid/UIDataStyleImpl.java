package cn.cerc.ui.grid;

import java.util.HashMap;

import cn.cerc.db.core.DataSource;
import cn.cerc.db.core.FieldMeta;

public interface UIDataStyleImpl extends DataSource {

    FieldStyleData addField(String fieldCode);
    
    HashMap<String, FieldStyleData> fields();

    Object setDefault(FieldMeta meta);

}
