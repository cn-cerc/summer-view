package cn.cerc.ui.grid;

import java.util.List;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldMeta;

public interface UIDataStyleImpl {

    DataSet dataSet();

    FieldMeta addField(String fieldCode);

    Object setDefault(FieldMeta fieldMeta);

    List<FieldMeta> fields();

}
