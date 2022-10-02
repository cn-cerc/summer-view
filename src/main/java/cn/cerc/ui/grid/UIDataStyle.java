package cn.cerc.ui.grid;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataCell;
import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
import cn.cerc.db.editor.OnGetText;
import cn.cerc.mis.ado.UsedEnum;
import cn.cerc.ui.vcl.UIInput;

public class UIDataStyle implements UIDataStyleImpl {
    private static final Logger log = LoggerFactory.getLogger(UIDataStyle.class);
    public boolean inputState = false;
    private DataSet dataSet;
    private List<FieldMeta> fields = new ArrayList<>();
    private OnOutput onOutput;

    public static int PX_SIZE = 14; // 1个汉字 = 14px

    public UIDataStyle() {
        this(false);
    }

    public UIDataStyle(boolean inputState) {
        super();
        this.inputState = inputState;
    }

    public boolean inputState() {
        return this.inputState;
    }

    public OnGetText getString() {
        return data -> {
            String result = data.getString();
            if (this.inputState) {
                UIInput input = new UIInput(null);
                input.setId(data.key());
                input.setValue(result);
                var meta = data.source().fields(data.key());
                if (meta.width() > 0) {
                    String width = String.format("%dpx", meta.width() * PX_SIZE);
                    input.setCssProperty("width", width);
                } else
                    input.setCssProperty("width", null);
                result = input.toString();
            }
            return result;
        };
    }

    private OnGetText getInteger() {
        return getString();
    }

    private OnGetText getDouble() {
        return getString();
    }

    public OnGetText getBoolean() {
        return data -> {
            return data.getBoolean() ? "是" : "";
        };
    }

    public OnGetText getBoolean(String trueText, String falseText) {
        return data -> {
            return data.getBoolean() ? trueText : falseText;
        };
    }

    public OnGetText getDatetime() {
        return data -> {
            return data.getDatetime().toString();
        };
    }

    public OnGetText getFastDate() {
        return data -> {
            return data.getFastDate().toString();
        };
    }

    public OnGetText getFastTime() {
        return data -> {
            return data.getFastTime().toString();
        };
    }

    @SuppressWarnings("rawtypes")
    public OnGetText getEnum(Class<? extends Enum> clazz) {
        return data -> {
            return data.getEnum(clazz).name();
        };
    }

    public OnGetText getList(List<String> items) {
        return data -> {
            return items.get(data.getInt());
        };
    }

    public interface OnOutput {
        OnGetText getOutputEvent(UIDataStyle style, FieldMeta meta);
    }

    public UIDataStyle onOutput(OnOutput onOutput) {
        this.onOutput = onOutput;
        return this;
    }

    @Override
    public UIDataStyle setDefault(FieldMeta meta) {
        if (meta.onGetText() == null) {
            var event = this.getDefault(meta);
            if (event != null)
                meta.onGetText(event);
        }
        return this;
    }

    public OnGetText getDefault(FieldMeta meta) {
        // 若有自定输出事件，为第一优先
        OnGetText result = null;
        if (onOutput != null)
            result = onOutput.getOutputEvent(this, meta);
        if (result != null)
            return result;

        // 根据数据类型输出
        var dataType = meta.dataType().dataType();
        if (dataType == null)
            return this.getString();

        return switch (dataType) {
        case "s" -> this.getString();
        case "b" -> this.getBoolean();
        case "n" -> this.getInteger();
        case "f" -> this.getDouble();
        case "d" -> this.getFastDate();
        case "t" -> this.getFastTime();
        case "dt" -> this.getDatetime();
        default -> {
            log.warn("Unexpected value: {}", dataType);
            yield this.getString();
        }
        };
    }

    @Override
    public FieldMeta addField(String fieldCode) {
        if (this.dataSet == null)
            throw new RuntimeException("dataSet is null");
        FieldMeta field = dataSet.fields().get(fieldCode);
        if (field == null)
            field = dataSet.fields().add(fieldCode, FieldKind.Calculated);
        this.fields.add(field);
        return field;
    }

    public FieldMeta addFieldIt() {
        return this.addField("it").onGetText(data -> "" + dataSet.recNo()).setName("序");
    }

    @Override
    public DataSet dataSet() {
        return dataSet;
    }

    public UIDataStyle setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    @Override
    public List<FieldMeta> fields() {
        return this.fields;
    }

    public static void main(String[] args) {
        var style = new UIDataStyle();
        var row = DataRow.of("code", 1);
        var code = row.fields().get("code");
        var data = new DataCell(row, code.code());

        style.onOutput((styld, meta) -> switch (meta.code()) {
        case "code" -> styld.getEnum(UsedEnum.class);
        default -> meta.onGetText();
        });

        System.out.println("output:" + style.getDefault(code).getText(data));
    }

}
