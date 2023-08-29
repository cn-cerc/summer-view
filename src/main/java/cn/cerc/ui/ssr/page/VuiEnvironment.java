package cn.cerc.ui.ssr.page;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import cn.cerc.db.core.ServerConfig;
import cn.cerc.db.core.Utils;
import cn.cerc.db.mongo.MongoConfig;
import cn.cerc.local.tool.JsonTool;
import cn.cerc.mis.core.AbstractForm;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.FormSign;
import cn.cerc.mis.core.IPage;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.mvc.StartForms;
import cn.cerc.ui.ssr.core.ISsrMessage;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.vcl.UIDiv;

public abstract class VuiEnvironment implements IVuiEnvironment {
    private static final Logger log = LoggerFactory.getLogger(VuiEnvironment.class);
    public static final String Visual_Menu = "visual-menu";
    protected AbstractForm form;
    protected String pageCode;
    private Map<Class<? extends VuiComponent>, Set<Class<? extends VuiComponent>>> customMap = new HashMap<>();
    private Map<Class<? extends VuiComponent>, Map<String, Object>> sourceMap = new HashMap<>();
    protected ISsrMessage onMessage;
    private UIComponent content;

    // 支持曾用类名
    private static final Map<String, String> AliasNames = new HashMap<>();
    static {
        AliasNames.put("SsrRedirectPage", "vuiRedirectPage");
        AliasNames.put("SsrDataRow", "vuiDataRow");
        AliasNames.put("SsrDataService", "vuiDataService");
        AliasNames.put("SsrReturnPage", "vuiReturnPage");
        AliasNames.put("SsrRequestRow", "vuiRequestRow");
        AliasNames.put("SsrPanel", "vuiPanel");
        AliasNames.put("SsrDataSet", "vuiDataSet");
        AliasNames.put("SsrMapServiceSource", "vuiMapService");
        AliasNames.put("ssrMapSource", "vuiMapService");
        AliasNames.put("SsrMapValueSource", "vuiMapValue");
        AliasNames.put("SsrMapEnumSource", "vuiMapSupplier");
        AliasNames.put("SsrButton", "vuiButton");
        AliasNames.put("VisualContainer", "vuiCanvas");
        AliasNames.put("UISsrForm", "vuiForm");
        AliasNames.put("UISsrGrid", "vuiGrid");
    }

    public void form(AbstractForm form) {
        this.form = form;
        if (form.getRequest() != null) {
            String childCode = StartForms.getRequestCode(form.getRequest());
            FormSign formSign = new FormSign(childCode);
            String methName = formSign.getValue();
            this.pageCode = form.getClass().getSimpleName() + "." + methName;
        } else {
            this.pageCode = "undefine";
        }
    }

    public IPage getPage() {
        var mode = form.getRequest().getParameter("mode");
        if ("design".equals(mode)) {
            if (form.getRequest().getParameter("submit") != null
                    || "submit".equals(form.getRequest().getParameter("opera")))
                saveEditor();
            return getDesignPage();
        } else if ("editor".equals(mode))
            return getEditorHtml();
        else if ("config".equals(mode))
            return loadFromDB();
        else if ("components".equals(mode))
            return getComponentsData();
        else {
            return getRuntimePage();
        }
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    /** 运行页面 */
    protected abstract IPage getRuntimePage();

    /** 设计页面 */
    protected abstract IPage getDesignPage();

    @Override
    public Class<?> getSupportClass() {
        return ISupportCanvas.class;
    }

    /**
     * 返回搜索页范例
     * 
     * @return
     */
    protected String getSampleData(String pageCode) {
        return "{}";
    }

    private void saveEditor() {
        var canvas = new VuiCanvas(this);
        // 初始化环境变量
        canvas.sendMessage(this, SsrMessage.InitRequest, form.getRequest(), null);
        canvas.sendMessage(this, SsrMessage.InitHandle, form, null);

        // 处理组件视图属性更新
        var reader = new RequestReader(form.getRequest());
        reader.updateComponents(canvas);
        reader.removeComponents(canvas);

        // 处理组件数据属性更新
        var targetId = form.getRequest().getParameter("id");
        if (!Utils.isEmpty(targetId)) {
            var owner = canvas.getMember(targetId, VuiComponent.class);
            if (owner.isPresent()) {
                owner.get().saveEditor(new RequestReader(form.getRequest()));
            } else {
                throw new RuntimeException(String.format("<div>组件id %s 没有找到！</div>", targetId));
            }
        }

        saveProperties(canvas.getProperties());
    }

    /** 属性页 */
    private IPage getEditorHtml() {
        var cid = form.getRequest().getParameter("id");
        PrintWriter writer;
        try {
            writer = form.getResponse().getWriter();
            writer.print("<div>");
            // 恢复现场
            var canvas = new VuiCanvas(this);
            // 初始化环境变量
            canvas.sendMessage(this, SsrMessage.InitRequest, form.getRequest(), null);
            canvas.sendMessage(this, SsrMessage.InitHandle, form, null);
            canvas.sendMessage(this, SsrMessage.InitBinder, null, null);
            var owner = canvas.getMember(cid, UIComponent.class);
            if (owner.isPresent()) {
                var target = owner.get();
                if (target instanceof VuiComponent impl) {
                    var div = new UIDiv(null);
                    impl.buildEditor(div, this.pageCode);
                    writer.print(div.toString());
                } else {
                    SsrBlock block = new SsrBlock("""
                            对不起，没有找到 ${class}(id=${id}) 相应的属性编辑器：
                            """);
                    block.toMap("class", target.getClass().getSimpleName());
                    block.toMap("id", cid);
                    writer.print(block.html());
                }
            } else {
                writer.print(String.format("<div>组件id %s 没有找到！</div>", cid));
            }
            writer.print("</div>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private IPage getComponentsData() {
        var cid = form.getRequest().getParameter("id");
        // 恢复现场
        var canvas = new VuiCanvas(this);
        // 初始化环境变量
        canvas.sendMessage(this, SsrMessage.InitRequest, form.getRequest(), null);
        canvas.sendMessage(this, SsrMessage.InitHandle, form, null);
        canvas.sendMessage(this, SsrMessage.InitBinder, null, null);
        // 输出可用组件
        PrintWriter writer;
        try {
            var mapper = new ObjectMapper();
            var root = mapper.createObjectNode();
            ArrayNode items = root.putArray("components");
            writer = form.getResponse().getWriter();
            Optional<VuiComponent> obj = canvas.getMember(cid, VuiComponent.class);
            if (obj.isPresent()) {
                if (obj.get() instanceof VuiContainer<?> impl) {
                    for (var item : impl.getChildren()) {
                        var title = item.getSimpleName();
                        var desc = item.getAnnotation(Description.class);
                        if (desc != null && !Utils.isEmpty(desc.value()))
                            title = desc.value();
                        // 加入到输出列表
                        var node = mapper.createObjectNode();
                        node.put("class", item.getSimpleName());
                        node.put("title", title);

                        boolean isContainer = false;
                        boolean isVisual = false;
                        for (var field : item.getDeclaredFields()) {
                            if (Modifier.isStatic(field.getModifiers())) {
                                if ("container".equals(field.getName()))
                                    isContainer = true;
                                if ("visual".equals(field.getName()))
                                    isVisual = true;
                            }
                        }
                        node.put("container", isContainer);
                        node.put("visual", isVisual);
                        var sign = impl.getComponentSign();
                        if (!Utils.isEmpty(sign))
                            node.put("sign", impl.getComponentSign());
                        items.add(node);
                    }
                }
            }
            writer.print(root.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 配置数据 */
    private IPage loadFromDB() {
        PrintWriter writer;
        try {
            writer = form.getResponse().getWriter();
            writer.print(loadProperties());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveProperties(String json) {
        String device = "";
        if (this.form.getRequest().getParameter("storage") != null)
            device = form.getRequest().getParameter("storage");
        MongoCollection<Document> collection = MongoConfig.getDatabase().getCollection(VuiEnvironment.Visual_Menu);
        ArrayList<Bson> match = new ArrayList<>();
        match.add(Filters.eq("corp_no_", form.getCorpNo()));
        match.add(Filters.eq("page_code_", pageCode));
        match.add(Filters.eq("device_", device));
        Bson bson = Filters.and(match);

        Document document = collection.find(bson).first();
        Document value = new Document();
        value.append("corp_no_", form.getCorpNo());
        value.append("page_code_", pageCode);
        value.append("device_", device);
        Document template = Document.parse(json);
        value.append("template_", template);
        if (document != null) {
            collection.replaceOne(bson, value);
        } else {
            collection.insertOne(value);
        }

        // 开发环境下将文件写入磁盘
        if (ServerConfig.isServerDevelop())
            writeFile(pageCode, device, json);
    }

    public void writeFile(String pageCode, String device, String template) {
        // 保存在用户根目录的 visual-menus 文件夹下，自行拷贝到项目对应的 resources
        String fileName = Utils.isEmpty(device) ? pageCode : String.join("-", pageCode, device);
        Path storage = Paths.get(System.getProperty("user.home"), "visual-menus", fileName + ".json");
        if (!Files.exists(storage)) {
            try {
                Files.createDirectories(storage.getParent());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storage.toFile(), StandardCharsets.UTF_8))) {
            writer.write(JsonTool.format(template));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 取得页面配置数据
     */
    @Override
    public String loadProperties() {
        MongoCollection<Document> collection = MongoConfig.getDatabase().getCollection(VuiEnvironment.Visual_Menu);
        Bson bson = Filters.and(Filters.eq("corp_no_", form.getCorpNo()), Filters.eq("page_code_", pageCode));

        String device = "";
        if (this.form.getRequest().getParameter("storage") != null)
            device = form.getRequest().getParameter("storage");
        Document documentDef = null;
        ArrayList<Document> documents = collection.find(bson).into(new ArrayList<>());
        for (Document document : documents) {
            if (Utils.isEmpty(document.getString("device_")))
                documentDef = document;
            if (device.equals(document.getString("device_"))) {
                Document value = document.get("template_", Document.class);
                return value.toJson();
            }
        }
        if (documentDef != null) {
            Document value = documentDef.get("template_", Document.class);
            return value.toJson();
        } else
            return getSampleData(pageCode);
    }

    public String loadFile(Class<?> clazz, String pageCode) {
        InputStream input = clazz.getClassLoader().getResourceAsStream(String.join("/", pageCode + ".json"));
        if (input == null)
            return "";

        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return builder.toString();
    }

    @Override
    public void attachClass(Class<? extends VuiComponent> ownerClass, Class<? extends VuiComponent> customClass) {
        Set<Class<? extends VuiComponent>> list = customMap.get(ownerClass);
        if (list == null) {
            list = new LinkedHashSet<>();
            customMap.put(ownerClass, list);
        }
        list.add(customClass);
    }

    @Override
    public Set<Class<? extends VuiComponent>> getAttachClass(Class<? extends VuiComponent> clazz) {
        var result = customMap.get(clazz);
        if (result != null)
            return result;
        return new LinkedHashSet<>();
    }

    @Override
    public Map<String, Object> getAttachData(Class<? extends VuiComponent> clazz) {
        var map = sourceMap.get(clazz);
        if (map != null)
            return map;
        return Map.of();
    }

    @Override
    public void attachData(Class<? extends VuiComponent> ownerClass, String dataId, Object data) {
        Map<String, Object> map = sourceMap.get(ownerClass);
        if (map == null) {
            map = new LinkedHashMap<>();
            sourceMap.put(ownerClass, map);
        }
        map.put(dataId, data);
    }

    /**
     * 在此可以插入自定义业务逻辑
     * 
     * @param onMessage
     */
    public void onMessage(ISsrMessage onMessage) {
        this.onMessage = onMessage;
    }

    @Override
    public <T> Optional<T> getBean(String beanId, Class<T> requiredType) {
        T result = null;
        var context = Application.getContext();
        if (context != null) {
            if (context.containsBean(beanId))
                result = context.getBean(beanId, requiredType);
            else {
                String temp = getBeanId(beanId);
                if (context.containsBean(temp))
                    result = context.getBean(temp, requiredType);
                else
                    log.error("找不到组件：{}", beanId);
            }
        } else
            log.error("无法创建对象：{}", beanId);
        return Optional.ofNullable(result);
    }

    @Override
    public String getBeanId(String beanId) {
        if (beanId.length() < 2)
            return beanId.toLowerCase();
        // 支持曾用过的类名
        for (var oldCode : AliasNames.keySet()) {
            if (oldCode.toLowerCase().equals(beanId.toLowerCase()))
                return AliasNames.get(oldCode);
        }
        var temp = beanId;
        var first = beanId.substring(0, 2);
        if (!first.toUpperCase().equals(first))
            temp = beanId.substring(0, 1).toLowerCase() + beanId.substring(1);
        return temp;
    }

    @Override
    public UIComponent getContent() {
        return content;
    }

    public void setContent(UIComponent content) {
        this.content = content;
    }

}
