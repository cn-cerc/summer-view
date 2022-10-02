package cn.cerc.ui.grid;

import java.util.List;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldMeta;

public interface UIFieldStyleImpl {

    Object setDefault(FieldMeta fieldMeta);

    DataSet dataSet();

    FieldMeta addField(String fieldCode);

    List<FieldMeta> fields();
    

}
