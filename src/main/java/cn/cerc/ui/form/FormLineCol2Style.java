package cn.cerc.ui.form;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import cn.cerc.db.core.DataRow;

public class FormLineCol2Style implements FormLineStyleImpl {
    private FormStyleImpl[] list = new FormStyleImpl[2];
    private DataRow dataRow;

    @Override
    public FormStyleImpl[] getList() {
        return list;
    }

    @Override
    public int getWidth() {
        return 12;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends FormStyleImpl> T addFormStyle(Class<T> formStyle, String code) {
        try {
            Constructor<? extends FormStyleImpl> constructor = formStyle.getDeclaredConstructor(String.class,
                    DataRow.class);
            constructor.setAccessible(true);
            FormStyleImpl styleImpl = constructor.newInstance(code, dataRow);
            addFormStyle(styleImpl);
            return (T) styleImpl;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends FormStyleImpl> T addFormStyle(Class<T> formStyle, String name, String code) {
        try {
            Constructor<? extends FormStyleImpl> constructor = formStyle.getDeclaredConstructor(String.class,
                    String.class, DataRow.class);
            constructor.setAccessible(true);
            FormStyleImpl styleImpl = constructor.newInstance(name, code, dataRow);
            addFormStyle(styleImpl);
            return (T) styleImpl;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends FormStyleImpl> T addFormStyle(Class<T> formStyle, String name, String code, int width) {
        try {
            Constructor<? extends FormStyleImpl> constructor = formStyle.getDeclaredConstructor(String.class,
                    String.class, int.class, DataRow.class);
            constructor.setAccessible(true);
            FormStyleImpl styleImpl = constructor.newInstance(name, code, width, dataRow);
            addFormStyle(styleImpl);
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

    private void addFormStyle(FormStyleImpl styleImpl) {
        if (list[0] == null)
            list[0] = styleImpl;
        else
            list[1] = styleImpl;
    }

}
