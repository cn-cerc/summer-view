package cn.cerc.ui.form;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import cn.cerc.db.core.DataRow;

public class FormLineCol1Style implements FormLineStyleImpl {
    private FormStyleImpl[] list = new FormStyleImpl[1];
    private DataRow dataRow;

    @Override
    public FormStyleImpl[] getList() {
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends FormStyleImpl> T addFormStyle(Class<T> formStyle, String code) {
        try {
            Constructor<? extends FormStyleImpl> constructor = formStyle.getDeclaredConstructor(String.class, DataRow.class);
            constructor.setAccessible(true);
            FormStyleImpl styleImpl = constructor.newInstance(code, dataRow);
            list[0] = styleImpl;
            return (T) styleImpl;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setDataRow(DataRow dataRow) {
        this.dataRow = dataRow;
    }

}
