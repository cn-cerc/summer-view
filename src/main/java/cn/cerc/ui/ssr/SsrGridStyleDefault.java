package cn.cerc.ui.ssr;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.cerc.mis.core.Application;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.grid.GridBooleanField;
import cn.cerc.ui.ssr.grid.GridCheckBoxField;
import cn.cerc.ui.ssr.grid.GridItField;
import cn.cerc.ui.ssr.grid.GridMapField;
import cn.cerc.ui.ssr.grid.GridOperaField;
import cn.cerc.ui.ssr.grid.GridStringField;

public class SsrGridStyleDefault implements SsrGridStyleImpl {
    private List<String> items = new ArrayList<>();
    private ImageConfigImpl imageConfig;

    public ISupplierBlock getIt() {
        return getIt("序", 2);
    }

    protected String getImage(String imgSrc) {
        if (imageConfig == null)
            imageConfig = Application.getBean(ImageConfigImpl.class);
        return imageConfig == null ? imgSrc : imageConfig.getCommonFile(imgSrc);
    }

    @Override
    public GridItField getIt(String title, int fieldWidth) {
        items.add(title);
        return new GridItField(title, fieldWidth);
    }

    @Override
    public GridOperaField getOpera(int fieldWidth) {
        String title = "操作";
        items.add(title);
        return new GridOperaField(fieldWidth);
    }

    @Override
    public GridStringField getDate(String title, String field) {
        return getString(title, field, 5, "center");
    }

    public GridStringField getDatetime(String title, String field) {
        return getString(title, field, 10, "center");
    }

    public GridStringField getDouble(String title, String field) {
        return getString(title, field, 4, "right");
    }

    @Override
    public GridMapField getMap(String title, String field, int fieldWidth, Map<String, String> map) {
        items.add(title);
        return new GridMapField(title, field, fieldWidth, map);
    }

    public ISupplierBlock getOption(String title, String field, int fieldWidth, Enum<?>[] enums) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Enum<?> item : enums) {
            map.put(String.valueOf(item.ordinal()), item.name());
        }
        return getMap(title, field, fieldWidth, map);
    }

    @Override
    public GridBooleanField getBoolean(String title, String field, int fieldWidth) {
        items.add(title);
        return new GridBooleanField(title, field, fieldWidth);
    }

    // TODO 要支持自定义 checkbox 的value
    @Override
    public GridCheckBoxField getCheckBox(String title, String field, int fieldWidth) {
        items.add(title);
        return new GridCheckBoxField(title, field, fieldWidth);
    }

    @Override
    public GridStringField getString(String title, String field, int fieldWidth) {
        items.add(title);
        var column = new GridStringField();
        column.title(title);
        column.field(field);
        column.fieldWidth = fieldWidth;
        column.align = "left";
        return column;
    }

    public GridStringField getString(String title, String field, int fieldWidth, String align) {
        items.add(title);
        var column = new GridStringField();
        column.title(title);
        column.field(field);
        column.fieldWidth = fieldWidth;
        column.align = align;
        return column;
    }

    @Override
    public List<String> items() {
        return items;
    }

}
