package cn.cerc.ui.ssr.core;

import java.lang.reflect.Modifier;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.ui.ssr.page.IVuiEnvironment;

public class PropertiesReader {
    private static final Logger log = LoggerFactory.getLogger(PropertiesReader.class);
    private JsonNode json;

    public PropertiesReader(String json) {
        try {
            this.json = new ObjectMapper().readTree(json);
        } catch (JsonProcessingException e) {
            log.error("error {}", e.getMessage(), e);
        }
    }

    public PropertiesReader(JsonNode json) {
        this.json = json;
    }

    public JsonNode root() {
        return json;
    }

    public Optional<JsonNode> get(String key) {
        return Optional.ofNullable(json.get(key));
    }

    public Optional<String> getString(String key) {
        var node = json.get(key);
        if (node == null)
            return Optional.empty();
        else
            return Optional.ofNullable(node.asText());
    }

    /** 从config-json还原组件 */
    public void read(VuiComponent self) {
        var id = this.getString("id");
        if (id.isPresent())
            self.setId(id.get());

        SsrUtils.copyNode(json, self.properties());

        for (var field : SsrUtils.getFieldList(self.getClass())) {
            if (!Modifier.isStatic(field.getModifiers()))
                SsrUtils.readProperty(self, field, json);
        }

        var node = this.get("components");
        if (node.isPresent()) {
            if (!node.get().isArray())
                throw new RuntimeException("components 必须为数组");
            IVuiEnvironment environment = self.canvas().environment();
            var components = node.get();
            for (var i = 0; i < components.size(); i++) {
                var child = components.get(i);
                var clazz = child.get("class");
                if (clazz == null)
                    log.error("clazz不允许为空");
                else {
                    var beanId = environment.getBeanId(clazz.asText());
                    var component = environment.getBean(beanId, VuiComponent.class);
                    if (component.isPresent()) {
                        component.get().canvas(self.canvas());
                        component.get().readProperties(new PropertiesReader(child));
                        self.addComponent(component.get());
                    } else {
                        log.error("无法创建对象：{}", beanId);
                    }
                }
            }
        }
    }

    public void read(DataRow dataRow) {
        var data = this.get("data");
        if (data.isPresent())
            readRow(data.get(), dataRow);
    }

    public void read(DataSet dataSet) {
        var data = this.get("data");
        if (data.isPresent() && data.get().isArray()) {
            for (var i = 0; i < data.get().size(); i++) {
                var item = data.get().get(i);
                dataSet.append();
                readRow(item, dataSet.current());
            }
        }
    }

    private void readRow(JsonNode value, DataRow dataRow) {
        var fields = value.fields();
        while (fields.hasNext()) {
            var item = fields.next();
            dataRow.setValue(item.getKey(), item.getValue().asText());
        }
    }

}
