package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;

public interface FormLineStyleImpl {

    void setDataRow(DataRow dataRow);

    int getWidth();

    FormStyleImpl[] getList();

    <T extends FormStyleImpl> T addFormStyle(Class<T> formStyle, String code);

    <T extends FormStyleImpl> T addFormStyle(Class<T> formStyle, String name, String code);
    
    <T extends FormStyleImpl> T addFormStyle(Class<T> formStyle, String name, String code, int width);
}
