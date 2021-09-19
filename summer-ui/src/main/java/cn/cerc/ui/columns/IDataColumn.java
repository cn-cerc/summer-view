package cn.cerc.ui.columns;

import cn.cerc.core.DataRow;

public interface IDataColumn extends IColumn {

    boolean isReadonly();

    Object setReadonly(boolean readonly);

    boolean isHidden();

    Object setHidden(boolean hidden);

    DataRow getRecord();

    void setRecord(DataRow record);
}
