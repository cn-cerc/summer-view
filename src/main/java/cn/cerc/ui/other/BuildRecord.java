package cn.cerc.ui.other;

import cn.cerc.db.core.DataRow;

public interface BuildRecord {
    void build(Object obj, DataRow row);
}
