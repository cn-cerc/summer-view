package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;

public interface FormLineStyleImpl {

    void setDataRow(DataRow dataRow);
    
    FormStyleImpl[] getList();

    <T extends FormStyleImpl> T addFormStyle(Class<T> formStyle, String code);
}
