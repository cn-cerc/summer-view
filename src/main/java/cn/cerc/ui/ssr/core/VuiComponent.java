package cn.cerc.ui.ssr.core;

import org.springframework.context.annotation.Description;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.editor.EditorForm;
import cn.cerc.ui.ssr.page.VuiCanvas;

@Description("组件")
public abstract class VuiComponent extends UIComponent implements ISsrMessage {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private ObjectNode properties = objectMapper.createObjectNode();
    private VuiCanvas canvas;

    public VuiComponent() {
        super(null);
    }

    public VuiComponent(UIComponent owner) {
        super(owner);
    }

    /** 取得一个html编辑器 */
    public void buildEditor(UIComponent content, String pageCode) {
        var form = new EditorForm(content, this);
        form.addProperties(this);
        form.build();
    }

    /** 在属性编辑器保存时执行 */
    public void saveEditor(RequestReader reader) {
        reader.sortComponent(this);
        // 写入当前组件属性
        reader.saveProperties(this);
        // 处理组件删除
        reader.removeComponent(this);
    }

    /** 将当前组件备份到config-json */
    public void writeProperties(PropertiesWriter writer) {
        writer.write(this);
    }

    /** 从config-json还原组件 */
    public void readProperties(PropertiesReader reader) {
        reader.read(this);
    }

    /** 创建一个新的id的前缀 */
    public String getIdPrefix() {
        return this.getClass().getSimpleName();
    }

    public VuiCanvas canvas() {
        return canvas;
    }

    public void canvas(VuiCanvas canvas) {
        this.canvas = canvas;
    }

    /** 接收消息 */
    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {

    }

    public ObjectNode properties() {
        if (!properties.has("v_top"))
            properties.put("v_top", 10);
        if (!properties.has("v_left"))
            properties.put("v_left", 10);
        return properties;
    }

}
