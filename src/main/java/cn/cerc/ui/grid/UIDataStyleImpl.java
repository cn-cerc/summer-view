package cn.cerc.ui.grid;

import java.util.List;

import cn.cerc.db.core.DataSource;
import cn.cerc.db.core.FieldMeta;

public interface UIDataStyleImpl extends DataSource {

    FieldMeta addField(String fieldCode);

    Object setDefault(FieldMeta meta);

    List<FieldMeta> fields();

}
