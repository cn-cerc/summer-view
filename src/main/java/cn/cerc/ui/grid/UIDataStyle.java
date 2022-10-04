package cn.cerc.ui.grid;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataCell;
import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
import cn.cerc.db.editor.OnGetText;
import cn.cerc.mis.ado.UsedEnum;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.UISelectDialog;
import cn.cerc.ui.vcl.UIInput;
import cn.cerc.ui.vcl.UISelect;

public class UIDataStyle implements UIDataStyleImpl {
    private static final Logger log = LoggerFactory.getLogger(UIDataStyle.class);
    private DataSet dataSet;
    private DataRow dataRow;
    private boolean readonly = true;
    private HashMap<String, FieldStyleData> items = new LinkedHashMap<>();
    private OnOutput onOutput;
    private Class<?> entityClass;
    private boolean grid;

    public static int PX_SIZE = 14; // 1个汉字 = 14px

    public UIDataStyle() {
        this(true);
    }

    public UIDataStyle(boolean readonly) {
        super();
        this.readonly = readonly;
    }

    @Override
    public boolean readonly() {
        return this.readonly;
    }

    public OnGetText getString() {
        return data -> {
            String result = data.getString();
            FieldStyleData styleData = this.items.get(data.key());
            if (!styleData.readonly()) {
                UIComponent box = new UIComponent(null);
                //
                UIInput input = new UIInput(box);
                input.setId(data.key());
                input.setValue(result);
                if (styleData.width() > 0) {
                    String width = String.format("width: %dpx", styleData.width() * PX_SIZE);
                    input.setCssStyle(width);
                } else
                    input.setCssStyle(null);
                input.setPlaceholder(styleData.placeholder());
                // 允许外部更改input组件的属性
                styleData.output(input);
                //
                if (styleData.dialog() != null)
                    new UISelectDialog(box).setDialog(styleData.dialog()).setInputId(data.key());
                //
                result = box.toString();
            } else if (!this.readonly()) {
                result = getDisplayText(styleData, result);
            }
            return result;
        };
    }

    private String getDisplayText(FieldStyleData styleData, String text) {
        if (this.grid)
            return text;
        UIComponent box = new UIComponent(null);
        UIInput input = new UIInput(box);
        input.setId(styleData.field().code());
        input.setValue(text);
        if (styleData.width() > 0) {
            String width = String.format("width: %dpx", styleData.width() * PX_SIZE);
            input.setCssStyle(width);
        } else
            input.setCssStyle(null);
        input.setReadonly(true);
        // 允许外部更改input组件的属性
        styleData.output(input);
        return box.toString();
    }

    private OnGetText getInteger() {
        return getString();
    }

    private OnGetText getDouble() {
        return getString();
    }

    public OnGetText getBoolean() {
        return getBoolean("是", "否");
    }

    public OnGetText getBoolean(String trueText, String falseText) {
        return data -> {
            String result = data.getBoolean() ? trueText : falseText;
            var styleData = this.items.get(data.key());
            if (!styleData.readonly()) {
                UIComponent box = new UIComponent(null);
                //
                UIInput input = new UIInput(box);
                input.setId(data.key());
                if (styleData.width() > 0) {
                    String width = String.format("width: %dpx", styleData.width() * PX_SIZE);
                    input.setCssStyle(width);
                } else
                    input.setCssStyle(null);
                input.setPlaceholder(styleData.placeholder());
                input.setInputType(UIInput.TYPE_CHECKBOX);
                input.setChecked(data.getBoolean());
                // 允许外部更改input组件的属性
                styleData.output(input);
                //
                if (styleData.dialog() != null)
                    new UISelectDialog(box).setDialog(styleData.dialog()).setInputId(data.key());
                //
                result = box.toString();
            } else if (!this.readonly()) {
                result = getDisplayText(styleData, result);
            }
            return result;
        };
    }

    public OnGetText getDatetime() {
        return data -> {
            String result = data.getDatetime().toString();
            if (data.getDatetime().isEmpty())
                result = "";
            var styleData = this.items.get(data.key());
            if (!styleData.readonly()) {
                UIComponent box = new UIComponent(null);
                //
                UIInput input = new UIInput(box);
                input.setId(data.key());
                input.setValue(result);
                if (styleData.width() > 0) {
                    String width = String.format("width: %dpx", styleData.width() * PX_SIZE);
                    input.setCssStyle(width);
                } else
                    input.setCssStyle(null);
                input.setPlaceholder(styleData.placeholder());
                input.setInputType(UIInput.TYPE_DATETIME_LOCAL);
                // 允许外部更改input组件的属性
                styleData.output(input);
                //
                if (styleData.dialog() != null)
                    new UISelectDialog(box).setDialog(styleData.dialog()).setInputId(data.key());
                //
                result = box.toString();
            } else if (!this.readonly()) {
                result = getDisplayText(styleData, result);
            }
            return result;
        };

    }

    public OnGetText getFastDate() {
        return data -> {
            String result = data.getFastDate().toString();
            if (data.getFastDate().isEmpty())
                result = "";
            var styleData = this.items.get(data.key());
            if (!styleData.readonly()) {
                UIComponent box = new UIComponent(null);
                //
                UIInput input = new UIInput(box);
                input.setId(data.key());
                input.setValue(result);
                if (styleData.width() > 0) {
                    String width = String.format("width: %dpx", styleData.width() * PX_SIZE);
                    input.setCssStyle(width);
                } else
                    input.setCssStyle(null);
                input.setPlaceholder(styleData.placeholder());
                input.setInputType(UIInput.TYPE_DATE);
                // 允许外部更改input组件的属性
                styleData.output(input);
                //
                if (styleData.dialog() != null)
                    new UISelectDialog(box).setDialog(styleData.dialog()).setInputId(data.key());
                //
                result = box.toString();
            } else if (!this.readonly()) {
                result = getDisplayText(styleData, result);
            }
            return result;
        };

    }

    public OnGetText getFastTime() {
        return data -> {
            String result = data.getFastTime().toString();
            if (data.getFastTime().isEmpty())
                result = "";
            FieldStyleData styleData = this.items.get(data.key());
            if (!styleData.readonly()) {
                UIComponent box = new UIComponent(null);
                //
                UIInput input = new UIInput(box);
                input.setId(data.key());
                input.setValue(result);
                if (styleData.width() > 0) {
                    String width = String.format("width: %dpx", styleData.width() * PX_SIZE);
                    input.setCssStyle(width);
                } else
                    input.setCssStyle(null);
                input.setPlaceholder(styleData.placeholder());
                // 允许外部更改input组件的属性
                styleData.output(input);
                //
                if (styleData.dialog() != null)
                    new UISelectDialog(box).setDialog(styleData.dialog()).setInputId(data.key());
                //
                result = box.toString();
            } else if (!this.readonly()) {
                result = getDisplayText(styleData, result);
            }
            return result;
        };
    }

    @SuppressWarnings("rawtypes")
    public OnGetText getEnum(Class<? extends Enum> clazz) {
        return data -> {
            String result = data.getEnum(clazz).name();
            var styleData = this.items.get(data.key());
            if (!styleData.readonly()) {
                UIComponent box = new UIComponent(null);
                //
                UISelect input = new UISelect(box);
                input.setId(data.key());
                for (var item : clazz.getEnumConstants())
                    input.getOptions().put("" + item.ordinal(), item.name());
                input.setSelected("" + data.getInt());
                // 允许外部更改input组件的属性
                styleData.output(input);
                //
                if (styleData.dialog() != null)
                    new UISelectDialog(box).setDialog(styleData.dialog()).setInputId(data.key());
                //
                return box.toString();
            } else if (!this.readonly()) {
                result = getDisplayText(styleData, result);
            }
            return result;
        };
    }

    public OnGetText getList(List<String> items) {
        return data -> {
            String result = items.get(data.getInt());
            var styleData = this.items.get(data.key());
            if (!styleData.readonly()) {
                UIComponent box = new UIComponent(null);
                //
                UISelect input = new UISelect(box);
                input.setId(data.key());
                int ordinal = 0;
                for (var item : items) {
                    input.getOptions().put("" + ordinal, item);
                    ordinal++;
                }
                input.setSelected("" + data.getInt());
                // 允许外部更改input组件的属性
                styleData.output(input);
                //
                if (styleData.dialog() != null)
                    new UISelectDialog(box).setDialog(styleData.dialog()).setInputId(data.key());
                //
                return box.toString();
            } else if (!this.readonly()) {
                result = getDisplayText(styleData, result);
            }
            return result;
        };
    }

    public OnGetText getMap(Map<String, String> items) {
        return data -> {
            String result = items.get(data.getString());
            var styleData = this.items.get(data.key());
            if (!styleData.readonly()) {
                UIComponent box = new UIComponent(null);
                //
                UISelect input = new UISelect(box);
                input.setId(data.key());
                for (var key : items.keySet())
                    input.getOptions().put(key, items.get(key));
                input.setSelected(data.getString());
                // 允许外部更改input组件的属性
                styleData.output(input);
                //
                if (styleData.dialog() != null)
                    new UISelectDialog(box).setDialog(styleData.dialog()).setInputId(data.key());
                //
                return box.toString();
            } else if (!this.readonly()) {
                result = getDisplayText(styleData, result);
            }
            return result;
        };
    }

    public interface OnOutput {
        OnGetText execute(FieldStyleData styleData);
    }

    public UIDataStyle onOutput(OnOutput onOutput) {
        this.onOutput = onOutput;
        return this;
    }

    @Override
    public boolean setDefault(FieldMeta meta) {
        boolean result = false;
        if (meta.onGetText() == null) {
            var event = this.getDefault(meta);
            if (event != null) {
                meta.onGetText(event);
                result = true;
            }
        }
        return result;
    }

    /**
     * 根据fieldMeta.dataType()返回不同的OnGetText函数
     * 若有定义onOutput事件，则先执行OnOutput事件函数，若OnOutput返回不为空，则以onOutput的返回值为主
     * 
     * @param fieldMeta 要设置的字段对象
     * @return 返回新的OnGetText事件函数
     */
    public OnGetText getDefault(FieldMeta fieldMeta) {
        // 若有自定输出事件，为第一优先
        OnGetText result = null;
        if (onOutput != null) {
            var style = items.get(fieldMeta.code());
            if (style == null) {
                style = new FieldStyleData(this, fieldMeta);
                items.put(fieldMeta.code(), style);
            }
            result = onOutput.execute(style);
        }
        if (result != null)
            return result;

        // 根据数据类型输出
        var dataType = fieldMeta.dataType().dataType();
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

    public UIDataStyle setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    @Override
    public FieldStyleData addField(String fieldCode) {
        if (this.current() == null)
            throw new RuntimeException("current is null");
        FieldMeta field = current().fields().get(fieldCode);
        if (field == null)
            field = current().fields().add(fieldCode);
        if (this.entityClass != null) {
            if (!field.readEntity(entityClass))
                field.setKind(FieldKind.Calculated);
        }
        var styleData = new FieldStyleData(this, field);
        styleData.setReadonly(this.readonly);
        this.items.put(fieldCode, styleData);
        return styleData;
    }

    /**
     * 
     * @return 于dataSet中增加一个it字段，并自动等于dataSet.recNo
     */
    public FieldMeta addFieldIt() {
        var dataSet = current().dataSet();
        if (dataSet == null) {
            log.error("没有找到dataSet");
            throw new RuntimeException("没有找到dataSet");
        }
        return this.addField("it").field().onGetText(data -> "" + dataSet.recNo()).setName("序");
    }

    public UIDataStyle setDataRow(DataRow dataRow) {
        if (this.dataSet != null)
            throw new RuntimeException("dataSet not is null");
        this.dataRow = dataRow;
        return this;
    }

    @Override
    public DataSet dataSet() {
        return dataSet;
    }

    @Override
    public DataRow current() {
        return dataSet != null ? dataSet.current() : dataRow;
    }

    public UIDataStyle setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    @Override
    public HashMap<String, FieldStyleData> fields() {
        return this.items;
    }

    public boolean grid() {
        return grid;
    }

    @Override
    public UIDataStyle setGrid(boolean grid) {
        this.grid = grid;
        return this;
    }

    public static void main(String[] args) {
        UIDataStyle style = new UIDataStyle();
        DataRow row = DataRow.of("code", 1);
        var code = row.fields().get("code");
        var data = new DataCell(row, code.code());

        style.onOutput(styleData -> switch (styleData.code()) {
        case "code" -> styleData.owner().getEnum(UsedEnum.class);
        default -> styleData.onGetText();
        });

        System.out.println("output:" + style.getDefault(code).getText(data));
    }

}
