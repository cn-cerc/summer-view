package cn.cerc.ui.form;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.DataRow;

public class FormStockStyle {
    // 标题
    private String caption;
    private DataRow dataRow;
    public List<FormLineStyleImpl> items = new ArrayList<>();

    public FormStockStyle(String caption, DataRow dataRow) {
        this.setCaption(caption);
        this.dataRow = dataRow;
    }

    @SuppressWarnings("unchecked")
    public <T extends FormLineStyleImpl> T addLine(Class<T> styleDefine) {
        try {
            Constructor<? extends FormLineStyleImpl> constructor = styleDefine.getDeclaredConstructor();
            constructor.setAccessible(true);
            FormLineStyleImpl styleImpl = constructor.newInstance();
            styleImpl.setDataRow(dataRow);
            items.add(styleImpl);
            return (T) styleImpl;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
