package cn.cerc.ui.grid;

import cn.cerc.db.core.DataType;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.editor.OnGetText;

/**
 * 字段显示样式数据定义
 * 
 * @author ZhangGong
 *
 */
public class FieldStyleData {
    private final FieldMeta field;
    private int width = 0; // 建议显示宽度
    private UIDataStyle owner;
    private String placeholder;

    public FieldStyleData(UIDataStyle owner, FieldMeta field) {
        this.owner = owner;
        this.field = field;
    }

    public UIDataStyle owner() {
        return this.owner;
    }

    public FieldMeta field() {
        return this.field;
    }

    public String code() {
        return this.field.code();
    }

    public OnGetText onGetText() {
        return this.field.onGetText();
    }

    public FieldStyleData onGetText(OnGetText onGetText) {
        this.field.onGetText(onGetText);
        return this;
    }

    public String name() {
        return this.field.name();
    }

    public FieldStyleData setName(String fieldName) {
        this.field.setName(fieldName);
        return this;
    }

    public int width() {
        return this.width;
    }

    public FieldStyleData setWidth(int width) {
        this.width = width;
        return this;
    }

    public DataType dataType() {
        return this.field.dataType();
    }

    public String placeholder() {
        return this.placeholder;
    }

    public FieldStyleData setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }
}
