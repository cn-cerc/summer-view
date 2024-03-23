package cn.cerc.ui.ssr.core;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.ui.ssr.source.Binder;

public class PropertiesWriter {
    private static final Logger log = LoggerFactory.getLogger(PropertiesWriter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private ObjectNode json;

    public PropertiesWriter() {
        this.json = objectMapper.createObjectNode();
    }

    public PropertiesWriter(ObjectNode json) {
        this.json = json;
    }

    public String json() {
        return json.toString();
    }

    /** 将当前组件备份到config-json */
    public void write(VuiComponent self) {
        json.put("class", self.getClass().getSimpleName());
        if (self.getId() != null)
            json.put("id", self.getId());

        SsrUtils.copyNode(self.properties(), json);

        var fields = SsrUtils.getFieldList(self.getClass());
        for (var field : fields)
            addField(self, field);

        if (self.getComponentCount() > 0) {
            var items = json.putArray("components");
            for (var item : self) {
                if (item instanceof VuiComponent impl)
                    impl.writeProperties(new PropertiesWriter(items.addObject()));
            }
        }
    }

    public void write(String key, String value) {
        json.put(key, value);
    }

    private void addField(Object object, Field field) {
        try {
            if (field.getType() == String.class)
                json.put(field.getName(), (String) field.get(object));
            else if (field.getType() == Boolean.class)
                json.put(field.getName(), (Boolean) field.get(object));
            else if (field.getType() == boolean.class)
                json.put(field.getName(), (boolean) field.get(object));
            else if (field.getType() == Integer.class)
                json.put(field.getName(), (Integer) field.get(object));
            else if (field.getType() == int.class)
                json.put(field.getName(), (int) field.get(object));
            else if (field.getType() == double.class)
                json.put(field.getName(), (double) field.get(object));
            else if (field.getType() == Double.class)
                json.put(field.getName(), (Double) field.get(object));
            else if (field.getType() == float.class)
                json.put(field.getName(), (float) field.get(object));
            else if (field.getType() == Float.class)
                json.put(field.getName(), (Float) field.get(object));
            else if (field.getType() == Binder.class) {
                Binder<?> binder = (Binder<?>) field.get(object);
                json.put(field.getName(), binder.targetId());
            } else if (field.getType().isEnum()) {
                Enum<?> value = (Enum<?>) field.get(object);
                if (value != null)
                    json.put(field.getName(), value.ordinal());
            } else if (field.getType() == EntityServiceRecord.class) {
                EntityServiceRecord record = (EntityServiceRecord) field.get(object);
                json.put(field.getName(), record.service());
                json.put(field.getName() + "_name", record.desc());
            } else {
                log.warn(String.format("put error: %s.%s", field.getType().getName(), field.getName()));
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void writer(DataRow dataRow) {
        var data = json.putObject("data");
        for (var field : dataRow.fields()) {
            data.put(field.code(), dataRow.getString(field.code()));
        }
    }

    public void write(DataSet dataSet) {

    }
}
