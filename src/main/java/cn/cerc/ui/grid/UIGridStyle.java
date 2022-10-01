package cn.cerc.ui.grid;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataCell;
import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.editor.OnGetText;
import cn.cerc.mis.ado.UsedEnum;
import cn.cerc.ui.vcl.UIInput;

public class UIGridStyle implements UIViewStyleImpl {
    private static final Logger log = LoggerFactory.getLogger(UIGridStyle.class);
    public boolean inputState = false;
    private OnOutput onOutput;

    public UIGridStyle() {
        this(true);
    }

    public UIGridStyle(boolean inputState) {
        super();
        this.inputState = inputState;
    }

    public boolean inputState() {
        return this.inputState;
    }

    public OnGetText getString() {
        return data -> {
            String result = data.getString();
            if (this.inputState)
                return UIInput.html(data.key(), result);
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
        OnGetText getOutputEvent(UIGridStyle sender, FieldMeta meta);
    }

    public void onOutput(OnOutput onOutput) {
        this.onOutput = onOutput;
    }

    @Override
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

    public static void main(String[] args) {
        var style = new UIGridStyle();
        var row = DataRow.of("code", 1);
        var code = row.fields().get("code");
        var data = new DataCell(row, code.code());

        style.onOutput((sender, meta) -> switch (meta.code()) {
        case "code" -> sender.getEnum(UsedEnum.class);
        default -> null;
        });

        System.out.println("output:" + style.getDefault(code).getText(data));
    }

}
