package cn.cerc.ui.ssr.grid;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.cerc.mis.core.Application;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.AlginEnum;
import cn.cerc.ui.ssr.core.ISupplierBlock;

public class SsrGridStyleDefault {
    private ImageConfigImpl imageConfig;

    public ISupplierBlock getIt() {
        return getIt("序", 2);
    }

    protected String getImage(String imgSrc) {
        if (imageConfig == null)
            imageConfig = Application.getBean(ImageConfigImpl.class);
        return imageConfig == null ? imgSrc : imageConfig.getCommonFile(imgSrc);
    }

    public GridItField getIt(String title, int fieldWidth) {
        return new GridItField(title, fieldWidth);
    }

    public GridOperaField getOpera(int fieldWidth) {
        return new GridOperaField(fieldWidth);
    }

    public GridStringField getDate(String title, String field) {
        return getString(title, field, 5, "center");
    }

    public GridStringField getDatetime(String title, String field) {
        return getString(title, field, 10, "center");
    }

    /**
     * 如果是value值为enum的下标的枚举请改为getNumber+toList方法，toList需要跟在getNumber之后
     * addBlock(style.getNumber('title', 'field', width).toList(enum.values()))
     * 如果value为非enum的下标的枚举请改为getString+toMap方法，toMap需要跟在getString之后
     * addBlock(style.getString('title', 'field', width).toMap(Map.of('', '')))
     */
    @Deprecated
    public GridMapField getMap(String title, String field, int fieldWidth, Map<String, String> map) {
        return new GridMapField(title, field, fieldWidth, map);
    }

    public ISupplierBlock getOption(String title, String field, int fieldWidth, Enum<?>[] enums) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Enum<?> item : enums) {
            map.put(String.valueOf(item.ordinal()), item.name());
        }
        return getMap(title, field, fieldWidth, map);
    }

    public GridBooleanField getBoolean(String title, int fieldWidth) {
        return new GridBooleanField(title, fieldWidth);
    }

    public GridBooleanField getBoolean(String title, String field, int fieldWidth) {
        return new GridBooleanField(title, field, fieldWidth);
    }

    /**
     * 请改使用 getBoolean
     * 
     * @param title
     * @param field
     * @param fieldWidth
     * @return
     */
    @Deprecated
    public GridCheckBoxField getCheckBox(String title, String field, int fieldWidth) {
        return new GridCheckBoxField(title, field, fieldWidth);
    }

    public GridStringField getString(String title, String field, int fieldWidth) {
        var column = new GridStringField();
        column.title(title);
        column.field(field);
        column.fieldWidth = fieldWidth;
        column.align = "left";
        return column;
    }

    public GridStringField getString(String title, String field, int fieldWidth, String align) {
        var column = new GridStringField();
        column.title(title);
        column.field(field);
        column.fieldWidth = fieldWidth;
        column.align = align;
        return column;
    }

    public GridNumberField getDouble(String title, String field) {
        return getNumber(title, field, 4).align(AlginEnum.center);
    }

    public GridNumberField getNumber(String title, String field, int fieldWidth) {
        var column = new GridNumberField();
        column.title(title);
        column.field(field);
        column.fieldWidth = fieldWidth;
        return column;
    }

}
