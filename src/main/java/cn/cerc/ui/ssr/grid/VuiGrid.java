package cn.cerc.ui.ssr.grid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Describe;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.TemplateConfigOptionEnum;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.base.UISsrBlock;
import cn.cerc.ui.ssr.core.AlignEnum;
import cn.cerc.ui.ssr.core.ISsrOption;
import cn.cerc.ui.ssr.core.ISsrTemplateConfig;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.PropertiesReader;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrTemplate;
import cn.cerc.ui.ssr.core.SummaryTypeEnum;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.EditorGrid;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.ISupportCanvas;
import cn.cerc.ui.ssr.page.IVuiEnvironment;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.Binders;
import cn.cerc.ui.ssr.source.IBinders;
import cn.cerc.ui.ssr.source.ISupplierFields;
import cn.cerc.ui.ssr.source.VuiDataService;
import cn.cerc.ui.style.IGridStyle;

/**
 * 第3代 SSR UI表格
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("数据表格")
public class VuiGrid extends VuiContainer<ISupportGrid> implements ISsrBoard, IGridStyle, IBinders, ISupportCanvas {
    private static final Logger log = LoggerFactory.getLogger(VuiGrid.class);
    private SsrTemplate template;
    private List<String> columns = new ArrayList<>();
    private Map<String, Consumer<SsrBlock>> onGetBodyHtml = new HashMap<>();
    private Map<String, Consumer<SsrBlock>> onGetHeadHtml = new HashMap<>();
    // 表样式 id
    public static final String TableBegin = "table.begin";
    public static final String TableEnd = "table.end";
    // 表头样式id
    public static final String HeadBegin = "head.begin";
    public static final String HeadEnd = "head.end";
    // 表身样式id
    public static final String BodyBegin = "body.begin";
    public static final String BodyEnd = "body.end";
    private SsrGridStyleDefault defaultStle;
    private Binders binders = new Binders();
    private HttpServletRequest request;
    @Column
    AlignEnum align = AlignEnum.Center;
    @Column(name = "当表格为空时显示内容")
    String emptyText = "";
    @Column
    Binder<VuiDataService> dataSet = new Binder<>(this, VuiDataService.class);

    public VuiGrid() {
        super();
        template = new SsrTemplate("");
    }

    public VuiGrid(UIComponent owner) {
        super(owner);
        template = new SsrTemplate("");
    }

    public VuiGrid(UIComponent owner, String templateText) {
        super(owner);
        template = new SsrTemplate(templateText);
    }

    public VuiGrid(UIComponent owner, Class<?> class1, String id) {
        super(owner);
        template = new SsrTemplate(class1, id);
    }

    @Deprecated
    public DataSet getDataSet() {
        return template.dataSet();
    }

    public DataSet dataSet() {
        return template.dataSet();
    }

    @Deprecated
    public VuiGrid setDataSet(DataSet dataSet) {
        return this.dataSet(dataSet);
    }

    public VuiGrid dataSet(DataSet dataSet) {
        template.dataSet(dataSet);
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.dataSet() == null) {
            html.print("<div>dataSet is null</div>");
            log.error("dataSet is null");
            return;
        }
        if (this.columns == null) {
            if (this.emptyText == null)
                html.print(String.format("dataSet.size=%d, fields is null, emptyText is null", getDataSet().size()));
            else
                html.print(this.emptyText);
            return;
        }

        getBlock(SsrTemplate.BeginFlag).ifPresent(template -> html.print(template.html()));
        getTemplate(TableBegin, getDefault_TableBegin()).ifPresent(value -> html.print(value.html()));

        // 输出标题
        getTemplate(HeadBegin, getDefault_HeadBegin()).ifPresent(value -> html.println(value.html()));
        for (var field : columns) {
            var block = getTemplate("head." + field, getDefault_HeadCell(field));
            if (block.isPresent()) {
                if (this.template.id() != null)
                    block.get().option(ISsrOption.TemplateId, this.template.id());
                var value = onGetHeadHtml.get(field);
                if (value != null)
                    value.accept(block.get().id(field));
            }
            block.ifPresent(value -> html.print(value.html()));
        }
        getTemplate(HeadEnd, () -> new SsrBlock("</tr>").template(template))
                .ifPresent(value -> html.println(value.html()));

        // 输出内容
        if (dataSet().size() > 0) {
            var save_rec = dataSet().recNo();
            try {
                dataSet().first();
                while (dataSet().fetch()) {
                    getTemplate(BodyBegin, getDefault_BodyBegin()).ifPresent(value -> html.println(value.html()));
                    for (var field : columns) {
                        var block = getTemplate("body." + field, getDefault_BodyCell(field));
                        if (block.isPresent()) {
                            var value = onGetBodyHtml.get(field);
                            if (value != null)
                                value.accept(block.get().id(field));
                        }
                        block.ifPresent(value -> html.print(value.html()));
                    }
                    getTemplate(BodyEnd, () -> new SsrBlock("</tr>").template(template))
                            .ifPresent(value -> html.println(value.html()));
                }

                Map<String, ISupportGrid> summaryFieldMap = new HashMap<>();
                for (UIComponent component : this) {
                    if (component instanceof ISupportGrid item) {
                        if (item.summaryType() != SummaryTypeEnum.无)
                            summaryFieldMap.put(item.title(), item);
                    }
                }

                if (summaryFieldMap.size() > 0) {
                    getTemplate(BodyBegin, getDefault_BodyBegin()).ifPresent(value -> html.println(value.html()));
                    for (String field : columns) {
                        ISupportGrid find = summaryFieldMap.get(field);
                        if (find != null) {
                            find.outputTotal(html, dataSet());
                        } else {
                            html.print("<td></td>");
                        }
                    }
                    getTemplate(BodyEnd, () -> new SsrBlock("</tr>").template(template))
                            .ifPresent(value -> html.println(value.html()));
                }
            } finally {
                dataSet().setRecNo(save_rec);
            }
        }

        getTemplate(TableEnd, () -> new SsrBlock("</table></div>").template(template))
                .ifPresent(value -> html.print(value.html()));
        getBlock(SsrTemplate.EndFlag).ifPresent(template -> html.print(template.html()));

    }

    private Optional<SsrBlock> getTemplate(String id, Supplier<SsrBlock> supplier) {
        SsrBlock block = template.getOrAdd(id, supplier).orElse(null);
        if (block != null)
            block.id(id);
        else
            log.error("表格模版中缺失定义：{}", id);
        return Optional.ofNullable(block);
    }

    /**
     * 请改使用 onGetHeadHtml
     * 
     * @param field
     * @param consumer
     */
    @Deprecated
    public void addGetHead(String field, Consumer<SsrBlock> consumer) {
        this.onGetHeadHtml(field, consumer);
    }

    public void onGetHeadHtml(String field, Consumer<SsrBlock> consumer) {
        this.onGetHeadHtml.put(field, consumer);
    }

    /**
     * 请改使用 onGetBody
     * 
     * @param field
     * @param consumer
     */
    @Deprecated
    public void addGetBody(String field, Consumer<SsrBlock> consumer) {
        this.onGetBodyHtml(field, consumer);
    }

    public void onGetBodyHtml(String field, Consumer<SsrBlock> consumer) {
        this.onGetBodyHtml.put(field, consumer);
    }

    public void onGetHtml(String field, Consumer<SsrBlock> consumer) {
        if (field.startsWith("head."))
            this.onGetHeadHtml(field.substring(5, field.length()), consumer);
        else if (field.startsWith("body."))
            this.onGetBodyHtml(field.substring(5, field.length()), consumer);
        else
            throw new RuntimeException("只支持以head.或body.开头的事件");
    }

    /**
     * 请改使用 columns
     * 
     * @return
     */
    @Deprecated
    public List<String> getFields() {
        return columns();
    }

    @Override
    public List<String> columns() {
        return columns;
    }

    @Deprecated
    public void setFields(List<String> columns) {
        this.columns = columns;
    }

    @Override
    public SsrTemplate template() {
        return template;
    }

    /**
     * 请改使用 addTemplate
     * 
     * @param id
     * @param templateText
     * @return
     */
    @Deprecated
    public VuiGrid putDefine(String id, String templateText) {
        addBlock(id, templateText);
        return this;
    }

    /**
     * 
     * @return 返回默认的表头样式
     */
    private Supplier<SsrBlock> getDefault_TableBegin() {
        return () -> new SsrBlock("<div id='grid' class='scrollArea'><table class='dbgrid'>").template(template);
    }

    /**
     * 
     * @return 返回表头行
     */
    private Supplier<SsrBlock> getDefault_HeadBegin() {
        return () -> new SsrBlock("<tr>").template(template);
    }

    /**
     * 
     * @return 返回表身行
     */
    private Supplier<SsrBlock> getDefault_BodyBegin() {
        return () -> new SsrBlock("<tr>").template(template);
    }

    /**
     * 
     * @param field
     * @return 返回默认的表头单元格样式
     */
    private Supplier<SsrBlock> getDefault_HeadCell(String field) {
        return () -> new SsrBlock(String.format("<th>%s</th>", field)).template(template);
    }

    /**
     * 
     * @param field
     * @return 返回默认的表身单元格样式
     */
    private Supplier<SsrBlock> getDefault_BodyCell(String field) {
        return () -> new SsrBlock(String.format("<td>${%s}</td>", field)).template(template);
    }

    @Deprecated
    public void addField(String... fields) {
        this.addColumn(fields);
    }

    /**
     * 请改使用 loadConfig
     * 
     * @return
     */
    public DataSet getDefaultOptions() {
        DataSet ds = new DataSet();
        for (var ssr : template) {
            var option = ssr.option(ISsrOption.Display);
            String id = ssr.id();
            if (option.isPresent()) {
                if (id.startsWith("body.") || id.startsWith("head."))
                    id = id.substring(5, id.length());
                if (ds.locate("column_name_", id)) {
                    if (ssr.id().startsWith("body."))
                        ds.edit().setValue("column_name_", id).setValue("option_", option.get());
                } else
                    ds.append().setValue("column_name_", id).setValue("option_", option.get());
            }
        }
        ds.head().setValue("template_id_", template.id());
        return ds;
    }

    public void loadConfig(IHandle handle) {
        var context = Application.getContext();
        var bean = context.getBean(ISsrTemplateConfig.class);
        for (var field : bean.getFields(handle, this.getDefaultOptions()))
            this.addField(field);
    }

    /**
     * 请改使用 loadConfig
     * 
     * @param configs
     */
    @Deprecated
    public void setConfig(DataSet configs) {
        configs.forEach(item -> {
            if (item.getEnum("option_", TemplateConfigOptionEnum.class) != TemplateConfigOptionEnum.不显示)
                addField(item.getString("column_name_"));
        });
    }

    public void loadDefaultConfig() {
        this.getDefaultOptions().forEach(item -> {
            if (item.getEnum("option_", TemplateConfigOptionEnum.class) != TemplateConfigOptionEnum.不显示)
                addColumn(item.getString("column_name_"));
        });
    }

    @Override
    public Optional<SsrBlock> getBlock(String blockId) {
        return template.get(blockId);
    }

    /**
     * 请改使用 emptyText函数
     * 
     * @return
     */
    public String getEmptyText() {
        return emptyText();
    }

    public String emptyText() {
        return this.emptyText;
    }

    /**
     * 请改使用 emptyText 函数
     * 
     * @param emptyText
     */
    @Deprecated
    public void setEmptyText(String emptyText) {
        this.emptyText(emptyText);
    }

    public void emptyText(String emptyText) {
        this.emptyText = emptyText;
    }

    /**
     * 请改使用 defaultStyle()
     * 
     * @return
     */
    @Deprecated
    public SsrGridStyleDefault createDefaultStyle() {
        return defaultStyle();
    }

    public SsrGridStyleDefault defaultStyle() {
        if (defaultStle == null)
            defaultStle = new SsrGridStyleDefault();
        return defaultStle;
    }

    /**
     * 请使用 templateId 函数
     * 
     * @param id
     * @return
     */
    @Deprecated
    public VuiGrid setTemplateId(String id) {
        template.id(id);
        return this;
    }

    public VuiGrid templateId(String id) {
        template.id(id);
        return this;
    }

    public String templateId() {
        return template.id();
    }

    @Override
    public void readProperties(PropertiesReader reader) {
        reader.read(this);
        for (var item : this) {
            var id = item.getId();
            if (Utils.isEmpty(id))
                log.warn("{} 没有 id，无法设置属性", item.getClass().getSimpleName());
            else {
                if (item instanceof ISupplierBlock supplier)
                    this.addBlock(supplier);
                if (item instanceof ISupportGrid impl) {
                    if (!Utils.isEmpty(impl.title()))
                        this.addColumn(impl.title());
                }
            }
        }
    }

    public void dataSourceBindId(String dataSetBindId) {
        this.dataSet.targetId(dataSetBindId != null ? dataSetBindId : "");
    }

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        super.buildEditor(content, pageCode);

        EditorGrid grid = new EditorGrid(content, this);
        grid.build(pageCode);

        DataSet dataSet = new DataSet();
        UISsrBlock impl = new UISsrBlock(content,
                """
                        <form method="post" id="fieldForm">
                            <input type="hidden" name="id" value=${id}>
                            <div id="grid" class="scrollArea">
                                <table class="dbgrid">
                                    <tbody>
                                        <tr>
                                            <th style="width: 4em">选择</th>
                                            <th style="width: 10em">字段</th>
                                            <th style="width: 20em">类名</th>
                                        </tr>
                                        ${dataset.begin}
                                        <tr>
                                            <td align="center" role="check">
                                                <span><input type="checkbox" name="components" value="${class},${field},${width},${title}"></span>
                                            </td>
                                            <td align="left" role="title">${title}</td>
                                            <td align="left" role="class">${class}</td>
                                        </tr>
                                        ${dataset.end}
                                    </tbody>
                                </table>
                            </div>
                            <div lowcode="button"><button name="save" value="save" onclick="submitForm('fieldForm', 'submit')">保存</button>
                            </div>
                        </form>""");
        impl.block().dataSet(dataSet);
        impl.block().option("id", this.getId());

        Optional<VuiDataService> optSvr = this.dataSet.target();
        dataSet.append()
                .setValue("field", "it")
                .setValue("title", "序")
                .setValue("class", GridItField.class.getSimpleName())
                .setValue("width", 4)
                .setValue("check", false);
        if (optSvr.isPresent()) {
            Set<Field> fields = optSvr.get().fields(ISupplierFields.BodyOutFields);
            for (Field field : fields) {
                if (dataSet.locate("field", field.getName()))
                    continue;
                String title = field.getName();
                Column column = field.getAnnotation(Column.class);
                if (column == null)
                    continue;
                if (!Utils.isEmpty(column.name()))
                    title = column.name();
                int width = 10;
                Describe describe = field.getAnnotation(Describe.class);
                if (describe != null) {
                    if (describe.width() > 0)
                        width = describe.width();
                }
                String classCode = GridStringField.class.getSimpleName();
                if (field.getType() == Boolean.class || field.getType() == boolean.class)
                    classCode = GridBooleanField.class.getSimpleName();
                else if (field.getType() == Integer.class || field.getType() == int.class
                        || field.getType() == Double.class || field.getType() == double.class
                        || field.getType().isEnum())
                    classCode = GridNumberField.class.getSimpleName();
                dataSet.append()
                        .setValue("field", field.getName())
                        .setValue("title", title)
                        .setValue("class", classCode)
                        .setValue("width", width)
                        .setValue("check", false);
            }
        }
    }

    @Override
    public void saveEditor(RequestReader reader) {
        reader.saveProperties(this);
        // 对栏位进行排序saveEditor
        reader.sortComponent(this);
        // 批量添加组件
        batchAppendComponents();
        // 处理移除组件
        reader.removeComponent(this);
    }

    private void batchAppendComponents() {
        String[] components = request.getParameterValues("components");
        if (Utils.isEmpty(components))
            return;
        IVuiEnvironment environment = this.canvas().environment();
        for (String component : components) {
            String[] componentProperties = component.split(",");
            String clazz = componentProperties[0];
            String field = componentProperties[1];
            int width = Utils.strToIntDef(componentProperties[2], 10);
            String title = componentProperties[3];
            Optional<VuiComponent> optBean = environment.getBean(clazz, VuiComponent.class);
            if (optBean.isEmpty())
                continue;
            VuiComponent item = optBean.get();
            item.setOwner(this);
            item.canvas(this.canvas());
            // 创建id
            String prefix = item.getIdPrefix();
            String nid = this.canvas().createUid(prefix);
            item.setId(nid);
            if (item instanceof ISupportGrid gridField) {
                gridField.title(title);
                gridField.field(field);
                gridField.width(width);
            }
            this.canvas().sendMessage(this, SsrMessage.appendComponent, item, this.dataSet.targetId());
        }
    }

    @Override
    public String getIdPrefix() {
        return "grid";
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitRequest:
            if (msgData instanceof HttpServletRequest request)
                this.request = request;
            break;
        case SsrMessage.InitBinder:
            this.dataSet.init();
            break;
        case SsrMessage.RefreshProperties:
        case SsrMessage.InitProperties:
        case SsrMessage.AfterSubmit:
            if (this.dataSet.target().isEmpty()) {
                log.warn("未设置数据源：dataSet");
                break;
            }
            var bean = this.canvas().getMember(this.dataSet.targetId(), VuiDataService.class);
            if (bean.isPresent())
                this.dataSet(bean.get().dataSet());
            else
                log.warn("{} 绑定的数据源 {} 找不到", this.getId(), this.dataSet.targetId());
            break;
        }
    }

    @Override
    public Binders binders() {
        return binders;
    }

}
