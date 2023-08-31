package cn.cerc.ui.ssr.editor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.EntityServiceRecord;
import cn.cerc.ui.ssr.core.SsrUtils;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.form.FormStringField;
import cn.cerc.ui.ssr.form.SsrFormStyleDefault;
import cn.cerc.ui.ssr.form.VuiForm;
import cn.cerc.ui.ssr.source.Binder;

public class EditorForm extends UIComponent {
    private static final Logger log = LoggerFactory.getLogger(EditorForm.class);
    private VuiForm form;
    private VuiComponent sender;

    public EditorForm(UIComponent content, VuiComponent sender) {
        super(content);
        this.sender = sender;
        form = new VuiForm(content);
        form.dataRow(new DataRow());
        form.strict(false);
        form.addBlock(VuiForm.FormStart, """
                <div>
                    <span onclick="toggleSearch(this)">数据属性</span>
                    <div class="searchFormButtonDiv">
                        <button name="submit" type="submit" value="1">保存</button>
                    </div>
                </div>
                """);
        form.addBlock("id", """
                <li>
                    <label for="id"><em>唯一标识</em></label>
                    <div>
                        <input type="text" id="id" name="id" value="${id}" readonly />
                        <span role="suffix-icon"></span>
                    </div>
                </li>
                 """.trim()).toMap("id", this.sender.getId());
        form.addColumn("id");

        form.addBlock("class", """
                <li>
                    <label for="id"><em>类名</em></label>
                    <div>
                        <input type="text" id="class" name="class" value="${class}" readonly />
                        <span role="suffix-icon"></span>
                    </div>
                </li>
                 """.trim()).toMap("class", this.sender.getClass().getSimpleName());
        form.addColumn("class");
    }

    public void addItem(String title, String field, String value) {
        if (field.startsWith("v_"))
            return;
        var style = form.defaultStyle();
        form.addBlock(style.getString(title, field));
        if (!Utils.isEmpty(title))
            form.addColumn(title);
        form.dataRow().setValue(field, value);
    }

    public void addMap(String title, String field, Map<String, String> map) {
        var style = form.defaultStyle();
        form.addBlock(style.getString(title, field).toMap(map));
        form.addColumn(title);
    }

    public void build() {
        form.loadDefaultConfig();
    }

    public void addProperties(VuiComponent sender) {
        var properties = sender.properties();
        var names = properties.fieldNames();
        while (names.hasNext()) {
            var name = names.next();
            this.addItem(name, name, properties.get(name).asText());
        }
        var fields = SsrUtils.getFieldList(sender.getClass());
        for (var field : fields) {
            if (!Modifier.isStatic(field.getModifiers()))
                addField(sender, field);
        }
    }

    private void addField(Object properties, Field field) {
        try {
            var title = field.getName();
            var column = field.getAnnotation(Column.class);
            if (column != null && !Utils.isEmpty(column.name()))
                title = column.name();
            if (field.getType() == String.class || field.getType() == int.class) {
                this.addItem(title, field.getName(), "" + field.get(properties));
            } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                var style = form.defaultStyle();
                form.addBlock(style.getBoolean(title, field.getName()));
                form.addColumn(title);
                form.dataRow().setValue(field.getName(), field.get(properties));
            } else if (field.getType() == Binder.class) {
                Binder<?> binder = (Binder<?>) field.get(properties);
                var style = form.defaultStyle();
                FormStringField mapField = style.getString(title, field.getName());
                var clazz = binder.targetType();
                this.sender.canvas().getMembers().forEach((id, ssr) -> {
                    if (clazz.isInstance(ssr))
                        mapField.toMap(id, id);
                });
                if (mapField.block().map() != null && mapField.block().map().size() == 0)
                    mapField.toMap("", "未查找到可用数据源");
                else
                    mapField.toMap("", "请选择数据源");
                form.addBlock(mapField);
                form.dataRow().setValue(field.getName(), binder.targetId());
            } else if (field.getType().isEnum()) {
                Enum<?>[] enums = (Enum<?>[]) field.getType().getEnumConstants();
                var style = form.defaultStyle();
                FormStringField mapField = style.getString(title, field.getName());
                for (Enum<?> item : enums)
                    mapField.toMap(item.name(), item.name());
                form.addBlock(mapField);
                Object obj = field.get(properties);
                if (obj != null)
                    form.dataRow().setValue(field.getName(), ((Enum<?>) obj).name());
            } else if (field.getType() == EntityServiceRecord.class) {
                SsrFormStyleDefault style = form.defaultStyle();
                form.addBlock(style.getCodeName(title, field.getName(), "showEntityServiceDialog"));
                Object svr = field.get(properties);
                if (svr != null && svr instanceof EntityServiceRecord record) {
                    form.dataRow()
                            .setValue(field.getName(), record.service())
                            .setValue(field.getName() + "_name", record.desc());
                }
            } else {
                log.warn("暂不支持的属性 {} of {}", field.getName(), field.getType().getSimpleName());
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public FormStringField addClassList(Class<?> class1, Class<?> defaultClass) {
        String column = "增加";
        FormStringField mapField = form.defaultStyle().getString(column, "appendComponent");
        if (defaultClass != null)
            form.dataRow().setValue(mapField.fields(), defaultClass.getSimpleName());
        if (sender instanceof VuiContainer<?> impl) {
            Set<Class<? extends VuiComponent>> children = impl.getChildren();
            for (var clazz : children) {
                var title = clazz.getSimpleName();
                Description desc = clazz.getAnnotation(Description.class);
                if (desc != null && !Utils.isEmpty(desc.value()))
                    title = desc.value();
                mapField.toMap(clazz.getSimpleName(), title);
            }
            form.addBlock(mapField);
            form.addColumn(column);
        }
        return mapField;
    }

    public VuiForm getForm() {
        return form;
    }

}
