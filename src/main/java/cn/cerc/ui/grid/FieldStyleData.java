package cn.cerc.ui.grid;

import cn.cerc.db.core.DataType;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.editor.OnGetText;
import cn.cerc.ui.core.UIComponent;

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
    private UIComponent executant;
    private OnOutput onOutput;

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

    /**
     * 设置 dataSet.fields(fieldCode).setName
     * 
     * @param fieldName
     * @return 自身
     */
    public FieldStyleData setName(String fieldName) {
        this.field.setName(fieldName);
        return this;
    }

    public int width() {
        return this.width;
    }

    /**
     * 设置在修改模式下的input输入框宽度，此处单位为1个汉字，即若要显示4个汉字，请设置为4
     * 
     * @param width
     * @return 自身
     */
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

    /**
     * 设置在修改模式下的input输入框提示
     * 
     * @param placeholder
     * @return 自身
     */
    public FieldStyleData setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public UIComponent executant() {
        return executant;
    }

    public interface OnOutput {
        void execute(FieldStyleData styleData);
    }

    /**
     * 设置在修改模式下，响应对输入组件的特殊处理
     * 
     * @param onOutput 响应输出事件，可在此设置如UIInput的属性
     * @return 自身
     */
    public FieldStyleData onOutput(OnOutput onOutput) {
        this.onOutput = onOutput;
        return this;
    }

    public void output(UIComponent executant) {
        this.executant = executant;
        if (onOutput != null)
            onOutput.execute(this);
    }
}
