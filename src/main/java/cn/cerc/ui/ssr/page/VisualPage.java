package cn.cerc.ui.ssr.page;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.context.annotation.Description;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import cn.cerc.db.core.Utils;
import cn.cerc.db.mongo.MongoConfig;
import cn.cerc.mis.core.AbstractForm;
import cn.cerc.mis.core.FormSign;
import cn.cerc.mis.core.IPage;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.mvc.StartForms;
import cn.cerc.ui.ssr.ISsrMessage;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.core.ISupportChildren;
import cn.cerc.ui.ssr.core.SsrComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.vcl.UIDiv;

public abstract class VisualPage implements ISupplierClassList {
//    private static final Logger log = LoggerFactory.getLogger(CustomVisualPage.class);
    public static final String Visual_Menu = "visual-menu";
    protected final AbstractForm form;
    protected final String pageCode;
    private Map<Class<? extends SsrComponent>, Set<Class<? extends SsrComponent>>> customMap = new HashMap<>();
    private Map<Class<? extends SsrComponent>, Map<String, Object>> sourceMap = new HashMap<>();
    protected ISsrMessage onMessage;

    public VisualPage(AbstractForm form) {
        super();
        this.form = form;
        String childCode = StartForms.getRequestCode(form.getRequest());
        FormSign formSign = new FormSign(childCode);
        String methName = formSign.getValue();
        this.pageCode = form.getClass().getSimpleName() + "." + methName;
    }

    public IPage getPage() {
        var mode = form.getRequest().getParameter("mode");
        if ("design".equals(mode)) {
            if (form.getRequest().getParameter("submit") != null
                    || "submit".equals(form.getRequest().getParameter("opera")))
                saveEditorData();
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

    /** 运行页面 */
    protected abstract IPage getRuntimePage();

    /** 设计页面 */
    protected abstract IPage getDesignPage();

    /**
     * 返回搜索页范例
     * 
     * @return
     */
    protected abstract Supplier<SsrBlock> getSampleData();

    private void saveEditorData() {
        // 恢复现场
        var container = new VisualContainer(null);
        container.supplierClassList(this);
        var json = getPageTemplate();
        container.setProperties(json);
        // 初始化环境变量
        container.sendMessage(this, SsrMessage.InitRequest, form.getRequest(), null);
        container.sendMessage(this, SsrMessage.InitHandle, form, null);

        // 处理组件更新（含增加）
        var reader = new RequestReader(form.getRequest());
        reader.updateComponents(container);

        // 更新组件属性
        var targetId = form.getRequest().getParameter("id");
        if (Utils.isEmpty(targetId))
            throw new RuntimeException("组件id不允许为空！");
        var owner = container.getMember(targetId, UIComponent.class);
        if (owner.isPresent()) {
            var target = owner.get();
            if (target instanceof SsrComponent impl) {
                if (impl.saveEditor(new RequestReader(form.getRequest()))) {
                    saveToMongoDB(container.getProperties());
                } else {
                    throw new RuntimeException(String.format("<div>组件%s(id=%s) 保存功能正在开发中</div>",
                            target.getClass().getSimpleName(), targetId));
                }
            } else {
                SsrBlock block = new SsrBlock("""
                        对不起，没有找到 ${class}(id=${id}) 相应的属性编辑器：
                        """);
                block.toMap("class", target.getClass().getSimpleName());
                block.toMap("id", targetId);
                throw new RuntimeException(block.getHtml());
            }
        } else {
            throw new RuntimeException(String.format("<div>组件id %s 没有找到！</div>", targetId));
        }
    }

    /** 属性页 */
    private IPage getEditorHtml() {
        var cid = form.getRequest().getParameter("id");
        PrintWriter writer;
        try {
            writer = form.getResponse().getWriter();
            writer.print("<div>");
            // 恢复现场
            var container = new VisualContainer(null);
            container.setProperties(getPageTemplate());
            // 初始化环境变量
            container.supplierClassList(this);
            container.sendMessage(this, SsrMessage.InitRequest, form.getRequest(), null);
            container.sendMessage(this, SsrMessage.InitHandle, form, null);
            container.sendMessage(this, SsrMessage.InitBinder, null, null);
            var owner = container.getMember(cid, UIComponent.class);
            if (owner.isPresent()) {
                var target = owner.get();
                if (target instanceof SsrComponent impl) {
                    var div = new UIDiv(null);
                    impl.buildEditor(div, this.pageCode);
                    writer.print(div.toString());
                } else {
                    SsrBlock block = new SsrBlock("""
                            对不起，没有找到 ${class}(id=${id}) 相应的属性编辑器：
                            """);
                    block.toMap("class", target.getClass().getSimpleName());
                    block.toMap("id", cid);
                    writer.print(block.getHtml());
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
        var container = new VisualContainer(null);
        container.setProperties(getPageTemplate());
        // 初始化环境变量
        container.supplierClassList(this);
        container.sendMessage(this, SsrMessage.InitRequest, form.getRequest(), null);
        container.sendMessage(this, SsrMessage.InitHandle, form, null);
        container.sendMessage(this, SsrMessage.InitBinder, null, null);
        // 输出可用组件
        PrintWriter writer;
        try {
            var mapper = new ObjectMapper();
            var root = mapper.createObjectNode();
            ArrayNode items = root.putArray("components");
            writer = form.getResponse().getWriter();
            Optional<SsrComponent> obj = container.getMember(cid, SsrComponent.class);
            if (obj.isPresent()) {
                if (obj.get() instanceof ISupportChildren impl) {
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
            writer.print(getPageTemplate());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveToMongoDB(String json) {
        MongoCollection<Document> collection = MongoConfig.getDatabase().getCollection(VisualPage.Visual_Menu);
        Bson bson = Filters.and(Filters.eq("corp_no_", form.getCorpNo()), Filters.eq("page_code_", pageCode));
        Document document = collection.find(bson).first();
        Document value = new Document();
        value.append("corp_no_", form.getCorpNo());
        value.append("page_code_", pageCode);
        Document template = Document.parse(json);
        value.append("template_", template);
        if (document != null) {
            collection.replaceOne(bson, value);
        } else {
            collection.insertOne(value);
        }
    }

    protected String getPageTemplate() {
        MongoCollection<Document> collection = MongoConfig.getDatabase().getCollection(VisualPage.Visual_Menu);
        Bson bson = Filters.and(Filters.eq("corp_no_", form.getCorpNo()), Filters.eq("page_code_", pageCode));
        Document document = collection.find(bson).first();
        if (document != null) {
            Document value = document.get("template_", Document.class);
            return value.toJson();
        }
        return getSampleData().get().toMap("pageCode", pageCode).getHtml();
    }

    @Override
    public void attachClass(Class<? extends SsrComponent> ownerClass, Class<? extends SsrComponent> customClass) {
        Set<Class<? extends SsrComponent>> list = customMap.get(ownerClass);
        if (list == null) {
            list = new LinkedHashSet<>();
            customMap.put(ownerClass, list);
        }
        list.add(customClass);
    }

    @Override
    public Set<Class<? extends SsrComponent>> getAttachClass(Class<? extends SsrComponent> clazz) {
        var result = customMap.get(clazz);
        if (result != null)
            return result;
        return new LinkedHashSet<>();
    }

    @Override
    public Map<String, Object> getAttachData(Class<? extends SsrComponent> clazz) {
        var map = sourceMap.get(clazz);
        if (map != null)
            return map;
        return Map.of();
    }

    @Override
    public void attachData(Class<? extends SsrComponent> ownerClass, String dataId, Object data) {
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

}
