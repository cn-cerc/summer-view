package cn.cerc.ui.ssr.page;

import java.io.BufferedInputStream;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Description;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import cn.cerc.db.core.FastDate;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.ISession;
import cn.cerc.db.core.ServerConfig;
import cn.cerc.db.core.Utils;
import cn.cerc.db.mongo.MongoConfig;
import cn.cerc.local.tool.JsonTool;
import cn.cerc.mis.core.AbstractForm;
import cn.cerc.mis.core.AppClient;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.FormSign;
import cn.cerc.mis.core.IPage;
import cn.cerc.mis.core.JsonPage;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UrlRecord;
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
    protected IHandle handle;
    protected boolean isRuntime;
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
        this.handle = form;
        if (form.getRequest() != null) {
            String childCode = StartForms.getRequestCode(form.getRequest());
            FormSign formSign = new FormSign(childCode);
            String methodName = formSign.getValue();
            this.pageCode = formSign.getId() + "." + methodName;
        } else {
            this.pageCode = "undefine";
        }
        // 查找是否有增加插件存在
        ApplicationContext context = Application.getContext();
        if (context != null) {
            for (var beanId : context.getBeanNamesForType(IVuiPlugins.class)) {
                var anno = context.findAnnotationOnBean(beanId, VuiMatches.class);
                try {
                    if (anno == null || (anno.value() != null && pageCode.matches(anno.value()))) {
                        context.getBean(beanId, IVuiPlugins.class).install(this, form);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public IPage getPage() {
        if (handle.allowVisualDesign()) {
            var mode = handle.getRequest().getParameter("mode");
            if ("design".equals(mode)) {
                if (handle.getRequest().getParameter("submit") != null
                        || "submit".equals(handle.getRequest().getParameter("opera")))
                    saveEditor();
                return getDesignPage();
            } else if ("editor".equals(mode))
                return getEditorHtml();
            else if ("config".equals(mode))
                return loadFromDB();
            else if ("components".equals(mode))
                return getComponentsData();
            else if ("import".equals(mode))
                return importJson();
            else if ("export".equals(mode))
                return exportJson();
            else if ("delete".equals(mode))
                return delete();
        }
        this.isRuntime = true;
        return getRuntimePage();
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    /**
     * 运行页面
     */
    protected abstract IPage getRuntimePage();

    /**
     * 设计页面
     */
    protected abstract IPage getDesignPage();

    @Override
    public Class<?> getSupportClass() {
        return ISupportCanvas.class;
    }

    /**
     * 返回搜索页范例
     *
     */
    protected String getSampleData(String pageCode) {
        return "{}";
    }

    protected void saveEditor() {
        var canvas = initCanvas();
        // 初始化环境变量
        canvas.sendMessage(this, SsrMessage.InitRequest, handle.getRequest(), null);
        canvas.sendMessage(this, SsrMessage.InitHandle, handle, null);

        // 处理组件视图属性更新
        var reader = new RequestReader(handle.getRequest());
        reader.updateComponents(canvas);
        reader.removeComponents(canvas);

        // 处理组件数据属性更新
        var targetId = handle.getRequest().getParameter("id");
        if (!Utils.isEmpty(targetId)) {
            var owner = canvas.getMember(targetId, VuiComponent.class);
            if (owner.isPresent()) {
                owner.get().saveEditor(new RequestReader(handle.getRequest()));
            } else {
                throw new RuntimeException(String.format("<div>组件id %s 没有找到！</div>", targetId));
            }
        }

        saveProperties(canvas.getProperties());
    }

    /**
     * 属性页
     */
    protected IPage getEditorHtml() {
        var cid = handle.getRequest().getParameter("id");
        PrintWriter writer;
        try {
            writer = handle.getSession().getResponse().getWriter();
            writer.print("<div>");
            // 恢复现场
            var canvas = initCanvas();
            // 初始化环境变量
            canvas.sendMessage(this, SsrMessage.InitRequest, handle.getRequest(), null);
            canvas.sendMessage(this, SsrMessage.InitHandle, handle, null);
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
            log.error(e.getMessage(), e);
        }
        return null;
    }

    protected IPage getComponentsData() {
        var cid = handle.getRequest().getParameter("id");
        // 恢复现场
        var canvas = initCanvas();
        // 初始化环境变量
        canvas.sendMessage(this, SsrMessage.InitRequest, handle.getRequest(), null);
        canvas.sendMessage(this, SsrMessage.InitHandle, handle, null);
        canvas.sendMessage(this, SsrMessage.InitBinder, null, null);
        // 输出可用组件
        PrintWriter writer;
        try {
            var mapper = new ObjectMapper();
            var root = mapper.createObjectNode();
            ArrayNode items = root.putArray("components");
            writer = handle.getSession().getResponse().getWriter();
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
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 配置数据
     */
    protected IPage loadFromDB() {
        PrintWriter writer;
        try {
            writer = handle.getSession().getResponse().getWriter();
            writer.print(loadProperties());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    protected IPage exportJson() {
        String json = JsonTool.format(loadProperties());
        HttpServletResponse response = form.getResponse();
        response.setContentType("application/octet-stream");
        String fileName = String.join("-", Utils.encode(pageCode, StandardCharsets.UTF_8.name()),
                new FastDate().format("yyyyMMdd"));
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".json");
        response.setContentLength(json.getBytes().length);
        PrintWriter writer;
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    protected IPage importJson() {
        HttpServletRequest request = form.getRequest();
        try {
            // 处理文件上传
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 获取所有文件列表
            List<FileItem> uploadFiles = upload.parseRequest(request);
            Optional<FileItem> file = uploadFiles.stream().filter(item -> !item.isFormField()).findFirst();
            if (file.isEmpty())
                return new JsonPage(form).setResultMessage(false, "导入失败：未上传文件");
            FileItem item = file.get();
            byte[] bytes = null;
            try (BufferedInputStream inputStream = new BufferedInputStream(item.getInputStream())) {
                bytes = inputStream.readAllBytes();
            }
            String json = new String(bytes);
            saveProperties(json);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new JsonPage(form).setResultMessage(false, "导入失败：" + e.getMessage());
        }
        return new JsonPage(form).setResultMessage(true, "导入成功");
    }

    protected IPage delete() {
        String device = "";
        if (this.handle.getRequest().getParameter("storage") != null)
            device = handle.getRequest().getParameter("storage");
        MongoCollection<Document> collection = MongoConfig.getDatabase().getCollection(table());
        ArrayList<Bson> match = new ArrayList<>();
        match.add(Filters.eq("corp_no_", corpNo()));
        match.add(Filters.eq("page_code_", pageCode));
        match.add(Filters.eq("device_", device));
        Bson bson = Filters.and(match);
        collection.deleteOne(bson);
        return new JsonPage(form).setResultMessage(true, "删除成功");
    }

    protected String getDesignUrl() {
        HttpServletRequest request = this.form.getRequest();
        Map<String, String[]> params = request.getParameterMap();
        UrlRecord url = new UrlRecord();
        url.setSite(pageCode);
        params.forEach((k, v) -> {
            if (ISession.TOKEN.equals(k) || "token".equals(k))
                return;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < v.length; i++) {
                builder.append(v[i]);
                if (i < v.length - 1) {
                    builder.append(",");
                }
            }
            url.putParam(k, builder.toString());
        });
        return url.getUrl();
    }

    @Override
    public void saveProperties(String json) {
        String device = "";
        if (this.handle.getRequest().getParameter("storage") != null)
            device = handle.getRequest().getParameter("storage");
        MongoCollection<Document> collection = MongoConfig.getDatabase().getCollection(table());
        ArrayList<Bson> match = new ArrayList<>();
        match.add(Filters.eq("corp_no_", corpNo()));
        match.add(Filters.eq("page_code_", pageCode));
        match.add(Filters.eq("device_", device));
        Bson bson = Filters.and(match);

        Document document = collection.find(bson).first();
        Document value = new Document();
        value.append("corp_no_", corpNo());
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
        String fileName = Utils.isEmpty(device) ? pageCode : String.join("_", pageCode, device);
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
        MongoCollection<Document> collection = MongoConfig.getDatabase().getCollection(table());
        Bson bson = Filters.and(Filters.eq("corp_no_", corpNo()), Filters.eq("page_code_", pageCode));

        String device = device();
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
        String device = device();
        String fileName = Utils.isEmpty(device) ? pageCode : String.join("_", pageCode, device);
        InputStream input = clazz.getClassLoader().getResourceAsStream(String.join("/", "menus", fileName + ".json"));
        if (input == null) {
            if (!Utils.isEmpty(device))// 兼容不带下划线_pc 的原始设计
                input = clazz.getClassLoader().getResourceAsStream(String.join("/", "menus", pageCode + ".json"));
            if (input == null)
                return "";
        }

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

    protected String device() {
        String device = "";
        if (this.handle.getRequest() == null)
            return device;
        if (this.handle.getRequest().getParameter("storage") != null)
            device = handle.getRequest().getParameter("storage");
        if (isRuntime) {
            AppClient client = new AppClient(handle.getRequest(), handle.getSession().getResponse());
            device = client.isPhone() ? "" : client.getDevice();
        }
        return device;
    }

    @Override
    public void attachClass(Class<? extends VuiComponent> ownerClass, Class<? extends VuiComponent> customClass) {
        customMap.computeIfAbsent(ownerClass, key -> new LinkedHashSet<>()).add(customClass);
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
        sourceMap.computeIfAbsent(ownerClass, key -> new LinkedHashMap<>()).put(dataId, data);
    }

    /**
     * 在此可以插入自定义业务逻辑
     *
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

    protected String corpNo() {
        return handle.getCorpNo();
    }

    @Override
    public UIComponent getContent() {
        return content;
    }

    public void setContent(UIComponent content) {
        this.content = content;
    }

    public boolean isRuntime() {
        return this.isRuntime;
    }

    protected VuiCanvas initCanvas() {
        return new VuiCanvas(this);
    }

    protected String table() {
        return Visual_Menu;
    }

}
