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
        this.caption = caption;
        this.dataRow = dataRow;
    }

    public FormLineStyleImpl addLine(Class<? extends FormLineStyleImpl> styleDefine) {
        try {
            Constructor<? extends FormLineStyleImpl> constructor = styleDefine.getDeclaredConstructor();
            constructor.setAccessible(true);
            FormLineStyleImpl styleImpl = constructor.newInstance();
            styleImpl.setDataRow(dataRow);
            items.add(styleImpl);
            return styleImpl;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
//        addLine(FromRadioStyle.class);
    }
}
