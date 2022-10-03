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

    public FieldStyleData(FieldMeta field) {
        this.field = field;
    }

    public FieldMeta field() {
        return this.field;
    }

    public FieldStyleData onGetText(OnGetText onGetText) {
        this.field.onGetText(onGetText);
        return this;
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
}
