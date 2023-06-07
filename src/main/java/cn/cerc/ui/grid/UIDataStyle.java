package cn.cerc.ui.grid;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataCell;
import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.FieldMeta.FieldKind;
import cn.cerc.db.core.Utils;
import cn.cerc.db.editor.OnGetText;
import cn.cerc.mis.ado.UsedEnum;
import cn.cerc.ui.vcl.UIInput;

public class UIDataStyle implements UIDataStyleImpl {
    private static final Logger log = LoggerFactory.getLogger(UIDataStyle.class);
    private DataSet dataSet;
    private boolean readonly = true;
    private HashMap<String, FieldStyleDefine> items = new LinkedHashMap<>();
    private OnOutput onOutput;
    private Class<?> entityClass;
    private boolean inGrid;

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
            return new UIStringDataStyle(this, data, this.inGrid).getText(data.getString());
        };
    }

    private OnGetText getInteger() {
        return data -> {
            return new UIStringDataStyle(this, data, this.inGrid).getText(data.getString());
        };
    }

    private OnGetText getDouble() {
        return data -> {
            return new UIStringDataStyle(this, data, this.inGrid).getText(data.getString());
        };
    }

    public OnGetText getBoolean() {
        return data -> {
            return new UIBooleanDataStyle(this, data, this.inGrid).getText(data.getString());
        };
    }

    public OnGetText getBoolean(String trueText, String falseText) {
        return data -> {
            UIBooleanDataStyle style = new UIBooleanDataStyle(this, data, this.inGrid);
            style.setTrueText(trueText);
            style.setFalseText(falseText);
            return style.getText(data.getString());
        };
    }

    public OnGetText getDatetime() {
        return data -> {
            String result = data.getDatetime().toString();
            if (data.getDatetime().isEmpty())
                result = "";
            var style = new UIStringDataStyle(this, data, this.inGrid);
            style.setInputType(UIInput.TYPE_DATETIME_LOCAL);
            return style.getText(result);
        };
    }

    public OnGetText getFastDate() {
        return data -> {
            String result = data.getFastDate().toString();
            if (data.getFastDate().isEmpty())
                result = "";
            var style = new UIStringDataStyle(this, data, this.inGrid);
            style.setInputType(UIInput.TYPE_DATE);
            return style.getText(result);
        };
    }

    public OnGetText getFastTime() {
        return data -> {
            String result = data.getFastTime().toString();
            if (data.getFastTime().isEmpty())
                result = "";
            var style = new UIStringDataStyle(this, data, this.inGrid);
            return style.getText(result);
        };
    }

    @SuppressWarnings("rawtypes")
    public OnGetText getEnum(Class<? extends Enum> clazz) {
        return getEnum(clazz, false);
    }

    @SuppressWarnings("rawtypes")
    public OnGetText getEnum(Class<? extends Enum> clazz, boolean addAll) {
        return data -> {
            String result = data.getEnum(clazz).name();
            var style = new UISelectDataStyle(this, data, this.inGrid);
            if (addAll)
                style.put("", "ALL");
            for (var item : clazz.getEnumConstants()) {
                style.put("" + item.ordinal(), item.name());
            }
            if (addAll && Utils.isEmpty(data.getString()))
                style.setSelected("");
            else
                style.setSelected("" + data.getInt());
            return style.getText(result);
        };
    }

    public OnGetText getList(List<String> items) {
        return data -> {
            String result = items.get(data.getInt());
            var style = new UISelectDataStyle(this, data, this.inGrid);
            int ordinal = 0;
            for (var item : items) {
                style.put("" + ordinal, item);
                ordinal++;
            }
            style.setSelected("" + data.getInt());
            return style.getText(result);
        };
    }

    public OnGetText getMap(Map<String, String> items, boolean addAll) {
        return data -> {
            String result = items.get(data.getString());
            var style = new UISelectDataStyle(this, data, this.inGrid);
            if (addAll)
                style.put("", "全部");
            for (var key : items.keySet())
                style.put(key, items.get(key));
            if (addAll && Utils.isEmpty(data.getString()))
                style.setSelected("");
            else
                style.setSelected(data.getString());
            return style.getText(result);
        };
    }

    public OnGetText getMap(Map<String, String> items) {
        return getMap(items, false);
    }

    public OnGetText getDate() {
        return data -> {
            String result = data.getString();
            var style = new UIDateDataStyle(this, data, this.inGrid);
            return style.getText(result);
        };
    }

    public interface OnOutput {
        OnGetText execute(FieldStyleDefine styleData);
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
                style = new FieldStyleDefine(fieldMeta);
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
    public FieldStyleDefine addField(String fieldCode) {
        var fields = this.dataSet.fields();
        if (fields == null)
            throw new RuntimeException("fields is null");
        FieldMeta field = fields.get(fieldCode);
        if (field == null)
            field = fields.add(fieldCode);
        if (this.entityClass != null) {
            if (!field.readEntity(entityClass))
                field.setKind(FieldKind.Calculated);
        }
        var styleData = this.items().get(fieldCode);
        if (styleData == null) {
            styleData = new FieldStyleDefine(field);
            // 如果第一次添加需要列，则自动赋初始值
            if (fieldCode == "it")
                styleData.setWidth(2).setAlignCenter().field().onGetText(data -> "" + dataSet.recNo()).setName("序");
            styleData.setReadonly(this.readonly);
            this.items.put(fieldCode, styleData);
        }
        return styleData;
    }

    /**
     * 
     * @return 于dataSet中增加一个it字段，并自动等于dataSet.recNo
     */
    public FieldStyleDefine addFieldIt() {
        var dataSet = dataSet();
        if (dataSet == null && current() != null)
            dataSet = current().dataSet();
        if (dataSet == null) {
            log.error("没有找到dataSet");
            throw new RuntimeException("没有找到dataSet");
        }
        var styleData = this.fields().get("it");
        if (styleData == null)
            styleData = this.addField("it");
        return styleData;
    }

    public UIDataStyle setDataRow(DataRow dataRow) {
        if (this.dataSet != null)
            throw new RuntimeException("dataSet not is null");
        this.dataSet = new DataSet();
        this.dataSet.fields().copyFrom(dataRow.fields());
        this.dataSet.records().add(dataRow);
        this.dataSet.setRecNo(1);
        dataRow.setDataSet(this.dataSet);
        return this;
    }

    @Override
    public Optional<DataSet> getDataSet() {
        return Optional.ofNullable(dataSet);
    }

    public DataSet dataSet() {
        return dataSet;
    }

    public DataRow current() {
        return dataSet.current();
    }

    public UIDataStyle setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    @Override
    public HashMap<String, FieldStyleDefine> fields() {
        return this.items;
    }

    public boolean inGrid() {
        return inGrid;
    }

    @Override
    public UIDataStyle setInGrid(boolean grid) {
        this.inGrid = grid;
        return this;
    }

    public HashMap<String, FieldStyleDefine> items() {
        return items;
    }

    @Override
    public FieldStyleDefine getFieldStyle(String fieldCode) {
        return items.get(fieldCode);
    }

    public static void main(String[] args) {
        UIDataStyle style = new UIDataStyle();
        DataRow row = DataRow.of("code", 1);
        var code = row.fields().get("code");
        var data = new DataCell(row, code.code());

        style.onOutput(styleData -> switch (styleData.code()) {
        case "code" -> style.getEnum(UsedEnum.class);
        default -> styleData.onGetText();
        });

        System.out.println("output:" + style.getDefault(code).getText(data));
    }

}
