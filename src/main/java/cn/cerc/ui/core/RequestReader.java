package cn.cerc.ui.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.TextNode;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.PropertiesReader;
import cn.cerc.ui.ssr.core.SsrUtils;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.page.IVuiEnvironment;
import cn.cerc.ui.ssr.page.VuiCanvas;
import cn.cerc.ui.ssr.source.Binder;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RequestReader {
    private static final Logger log = LoggerFactory.getLogger(RequestReader.class);
    private HttpServletRequest request;

    public RequestReader(HttpServletRequest request) {
        this.request = request;
    }

    public Optional<String> getString(String key) {
        return Optional.ofNullable(request.getParameter(key));
    }

    public VuiComponent sortComponent(VuiComponent owner) {
        var sort = this.getString("sort");
        if (sort.isEmpty())
            return null;
        String[] sortArr = sort.get().split(",");
        if (Utils.isEmpty(sortArr))
            return null;
        Map<String, Integer> scoreMap = new HashMap<>();
        for (int i = 0; i < sortArr.length; i++)
            scoreMap.put(sortArr[i], i);
        owner.getComponents().sort((o1, o2) -> scoreMap.get(o1.getId()) - scoreMap.get(o2.getId()));
        return null;
    }

    public void saveProperties(VuiComponent sender) {
        var properties = sender.properties();
        var names = this.request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = this.request.getParameter(name);
            if (name.startsWith("v_")) {
                if (value != null)
                    properties.put(name, value);
            }
        }

        for (var field : SsrUtils.getFieldList(sender.getClass()))
            if (!Modifier.isStatic(field.getModifiers()))
                addField(sender, field);
    }

    public VuiComponent removeComponent(VuiComponent owner) {
        var removeComponent = this.getString("removeComponent");
        if (removeComponent.isEmpty())
            return null;
        for (var item : owner) {
            if (removeComponent.get().equals(item.getId()) && item instanceof VuiComponent result) {
                owner.removeComponent(item);
                return result;
            }
        }
        return null;
    }

    public void updateComponents(VuiCanvas container) {
        var newItems = this.getString("updateComponents");
        if (newItems.isEmpty())
            return;
        // 找到存在变动的容器对象
        PropertiesReader reader = new PropertiesReader(newItems.get());
        var root = reader.root();
        if (root.isArray()) {
            for (var i = 0; i < root.size(); i++)
                updateComponent(container, root.get(i));
        }
        log.error("updateComponents 数据格式错误，必须为数组");
    }

    // 更新指定的容器
    private void updateComponent(VuiCanvas root, JsonNode objectData) {
        if (objectData.has("id")) {
            // 修改已有对象
            String objectId = objectData.get("id").asText();
            var ownerMember = root.getMember(objectId, VuiComponent.class);
            if (ownerMember == null) {
                log.error("{} 容器组件找不到", objectId);
                return;
            }
            var member = root.getMember(objectId, VuiComponent.class);
            if (member.isPresent()) {
                writerViewConfig(member.get(), objectData);
            } else {
                log.error("{} 组件找不到", objectId);
            }
        } else {
            // 增加新的对象
            if (objectData.has("owner_id") && objectData.has("class")) {
                String ownerId = objectData.get("owner_id").asText();
                String className = objectData.get("class").asText();
                var owner = root.getMember(ownerId, VuiComponent.class);
                if (owner != null) {
                    var beanId = root.environment().getBeanId(className);
                    var child = addComponent(owner.get(), beanId);
                    if (child != null) {
                        writerViewConfig(child, objectData);
                    } else {
                        log.error("{} 组件找不到", beanId);
                    }
                } else {
                    log.error("{} 容器组件找不到", ownerId);
                }
            } else {
                log.error("owner_id 与 class 不得为空");
            }
        }
    }

    private void writerViewConfig(VuiComponent child, JsonNode classData) {
        SsrUtils.copyNode(classData, child.properties());
        for (var field : SsrUtils.getFieldList(child.getClass()))
            SsrUtils.readProperty(child, field, classData);
    }

    private VuiComponent addComponent(VuiComponent owner, String appendComponent) {
        IVuiEnvironment environment = owner.canvas().environment();
        Optional<VuiComponent> optBean = environment.getBean(appendComponent, VuiComponent.class);
        if (optBean.isEmpty())
            return null;
        VuiComponent item = optBean.get();
        item.setOwner(owner);
        item.canvas(owner.canvas());
        // 创建id
        var prefix = item.getIdPrefix();
        var nid = owner.canvas().createUid(prefix);
        item.setId(nid);
        return item;
    }

    private void addField(Object properties, Field field) {
        try {
            if (field.getType() == String.class || field.getType() == Integer.class || field.getType() == int.class) {
                var temp = this.request.getParameter(field.getName());
                if (temp != null) {
                    JsonNode value = new TextNode(temp);
                    SsrUtils.writeToObject(properties, field, value);
                }
            } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                var temp = this.request.getParameter(field.getName());
                JsonNode value = ("1".equals(temp) || "true".equals(temp)) ? BooleanNode.TRUE : BooleanNode.FALSE;
                SsrUtils.writeToObject(properties, field, value);
            } else if (field.getType() == Binder.class) {
                Binder<?> binder = (Binder<?>) field.get(properties);
                var temp = this.request.getParameter(field.getName());
                if (temp != null)
                    binder.targetId(temp);
            } else if (field.getType().isEnum()) {
                var temp = this.request.getParameter(field.getName());
                if (temp != null) {
                    Enum<?>[] enums = (Enum<?>[]) field.getType().getEnumConstants();
                    Enum<?> defaultValue = enums[0];
                    for (Enum<?> item : enums) {
                        if (item.name().equals(temp)) {
                            defaultValue = item;
                            break;
                        }
                    }
                    field.set(properties, defaultValue);
                }
            } else {
                log.warn("暂不支持的数据字段类型：{}({})", field.getClass().getSimpleName(), field.getName());
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
