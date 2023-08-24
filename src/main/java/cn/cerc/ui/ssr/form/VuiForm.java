package cn.cerc.ui.ssr.form;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

import com.fasterxml.jackson.databind.node.ObjectNode;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IPage;
import cn.cerc.mis.other.MemoryBuffer;
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

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("查询表单")
public class VuiForm extends VuiContainer<ISupportForm>
        implements ISsrBoard, ISupplierDataRow, IBinders, ISupportCanvas {
    private static final Logger log = LoggerFactory.getLogger(VuiForm.class);
    private SsrTemplate template;
    private List<String> columns = new ArrayList<>();
    public static final String FormBegin = "form.begin";
    public static final String FormEnd = "form.end";
    public static final String FormStart = "formStart";
    private Map<String, Consumer<SsrBlock>> onGetHtml = new HashMap<>();
    private MemoryBuffer buffer;
    private SsrFormStyleDefault defaultStle;
    private HttpServletRequest request;
    private Binders binders = new Binders();
    @Column(name = "动作(action)")
    String action = "";
    @Column
    Binder<ISupplierDataRow> dataRow = new Binder<>(this, ISupplierDataRow.class);
    @Column
    AlignEnum align = AlignEnum.None;

    public VuiForm() {
        this(null);
    }

    public VuiForm(UIComponent owner) {
        super(owner);
        template = new SsrTemplate("");
        init(findOwner(IPage.class));
    }

    public VuiForm(UIComponent owner, String templateText) {
        super(owner);
        template = new SsrTemplate(templateText);
        init(findOwner(IPage.class));
    }

    public VuiForm(UIComponent owner, Class<?> class1, String id) {
        super(owner);
        template = new SsrTemplate(class1, id);
        init(findOwner(IPage.class));
    }

    private VuiForm init(IPage page) {
        this.setId("form1");
        if (page != null) {
            for (var block : template)
                block.option(ISsrOption.Phone, "" + page.getForm().getClient().isPhone());
        }
        return this;
    }

    @Override
    public DataRow dataRow() {
        return template.dataRow();
    }

    public VuiForm dataRow(DataRow dataRow) {
        this.template.dataRow(dataRow);
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.dataRow() == null) {
            html.print("<div>dataRow is null</div>");
            log.error("dataRow is null");
            return;
        }
        if (this.columns == null)
            this.columns = this.dataRow().fields().names();

        getBlock(SsrTemplate.BeginFlag).ifPresent(template -> html.print(template.html()));

        var top = getBlock(FormBegin, getDefault_FormBegin()).get();
        if (this.template.id() != null)
            top.option(ISsrOption.TemplateId, this.template.id());
        html.print(top.html());

        for (var column : columns) {
            var item = getBlock(column);
            if (item.isPresent()) {
                var block = item.get();
                block.template(template);
                Consumer<SsrBlock> value = onGetHtml.get(column);
                if (value != null)
                    value.accept(block.id(column));
                html.print(block.html());
            } else {
                html.print(new SsrBlock(
                        String.format("%s: <input type=\"text\" name=\"%s\" value=\"${%s}\">", column, column, column))
                        .template(template)
                        .html());
                log.error("找不到数据列: {}", column);
            }
        }
        getBlock(FormEnd, () -> new SsrBlock("</ul></form>").template(template))
                .ifPresent(value -> html.print(value.html()));
        getBlock(SsrTemplate.EndFlag).ifPresent(template -> html.print(template.html()));
    }

    public void onGetHtml(String field, Consumer<SsrBlock> consumer) {
        this.onGetHtml.put(field, consumer);
    }

    private Supplier<SsrBlock> getDefault_FormBegin() {
        var action = this.template().option("action").orElse("");
        return () -> {
            var ssr = new SsrBlock(String.format("<form method='post' action='%s'%s role='${role}'>${callback(%s)}<ul>",
                    action, !Utils.isEmpty(getId()) ? " id='" + getId() + "'" : "", VuiForm.FormStart))
                    .template(template);
            ssr.onCallback(VuiForm.FormStart, () -> {
                var formFirst = this.getBlock(VuiForm.FormStart);
                formFirst.ifPresent(template -> {
                    if (this.template.id() != null)
                        template.option(ISsrOption.TemplateId, this.template.id());
                });
                return formFirst.isPresent() ? formFirst.get().html() : "";
            });
            ssr.option("role", "search");
            return ssr;
        };
    }

    private Optional<SsrBlock> getBlock(String id, Supplier<SsrBlock> supplier) {
        SsrBlock block = template.getOrAdd(id, supplier).orElse(null);
        if (block != null)
            block.id(id);
        else
            log.error("表单模版中缺失定义：{}", id);
        return Optional.ofNullable(block);
    }

    /** 请改使用 columns 函数 */
    @Deprecated
    public List<String> fields() {
        return columns();
    }

    /**
     * 请改使用 buffer 函数
     * 
     * @param buffer
     */
    @Deprecated
    public void setBuffer(MemoryBuffer buffer) {
        buffer(buffer);
    }

    public VuiForm buffer(MemoryBuffer buffer) {
        this.buffer = buffer;
        return this;
    }

    public MemoryBuffer buffer() {
        return buffer;
    }

    public boolean readAll(HttpServletRequest request, String submitId) {
        return readAll(request, submitId, null);
    }

    public boolean readAll(HttpServletRequest request, String submitId, String submitVal) {
        if (dataRow() == null)
            this.dataRow(new DataRow());
        boolean submit = request.getParameter(submitId) != null
                && (submitVal == null || submitVal.equals(request.getParameter(submitId)));
        for (var ssr : this.template) {
            ssr.option("fields").ifPresent(fields1 -> {
                for (var field : fields1.split(",")) {
                    if (!Utils.isEmpty(field)) {
                        String val = request.getParameter(field);
                        if (val != null)
                            val = val.trim();
                        updateValue(field, val, submit);
                    }
                }
            });
        }
        return submit;
    }

    private void updateValue(String field, String val, boolean submit) {
        if (submit) {
            dataRow().setValue(field, val == null ? "" : val);
            if (buffer != null) {
                buffer.setValue(field, val);
            }
        } else {
            if (val != null) {
                dataRow().setValue(field, val);
            } else if (buffer != null && !buffer.isNull() && buffer.getRecord().exists(field)) {
                dataRow().setValue(field, buffer.getString(field));
            }
        }
    }

    /**
     * 请改使用 loadConfig
     * 
     * @return
     */
    public DataSet getDefaultOptions() {
        var template_id = template.id();
        DataSet ds = new DataSet();
        for (var ssr : template) {
            var option = ssr.option(ISsrOption.Display);
            if (option.isPresent() && !Utils.isEmpty(ssr.id()))
                ds.append().setValue("column_name_", ssr.id()).setValue("option_", option.get());
        }
        ds.head().setValue("template_id_", template_id);
        return ds;
    }

    public void loadConfig(IHandle handle) {
        var context = Application.getContext();
        var bean = context.getBean(ISsrTemplateConfig.class);
        for (var field : bean.getFields(handle, this.getDefaultOptions()))
            this.addColumn(field);
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
                addColumn(item.getString("column_name_"));
        });
    }

    public void loadDefaultConfig() {
        this.getDefaultOptions().forEach(item -> {
            if (item.getEnum("option_", TemplateConfigOptionEnum.class) != TemplateConfigOptionEnum.不显示)
                addColumn(item.getString("column_name_"));
        });
    }

    /**
     * 请改使用 defaultStyle()
     * 
     * @return
     */
    @Deprecated
    public SsrFormStyleDefault createDefaultStyle() {
        return defaultStyle();
    }

    public SsrFormStyleDefault defaultStyle() {
        if (defaultStle == null)
            defaultStle = new SsrFormStyleDefault();
        return defaultStle;
    }

    @Override
    public SsrTemplate template() {
        return this.template;
    }

    public String action() {
        return option("action").orElse("");
    }

    public VuiForm action(String action) {
        option("action", action);
        return this;
    }

    public VuiForm templateId(String id) {
        template.id(id);
        return this;
    }

    public String templateId() {
        return template.id();
    }

    public VuiForm role(String role) {
        this.option("role", role);
        return this;
    }

    /**
     * 请改使用 role 函数
     * 
     * @return
     */
    @Deprecated
    public VuiForm modify() {
        role("modify");
        return this;
    }

    /** 请改使用 getBlock */
    @Deprecated
    public Optional<SsrBlock> getTemplate(String blockId) {
        return this.getBlock(blockId);
    }

    /** 请改使用 template 函数 */
    @Deprecated
    public SsrTemplate define() {
        return template();
    }

    /** 请改使用 addColumn 函数 */
    @Deprecated
    public void addField(String name) {
        this.addColumn(name);
    }

    @Override
    public ISsrBoard addColumn(String... columns) {
        return ISsrBoard.super.addColumn(columns);
    }

    @Override
    public List<String> columns() {
        return columns;
    }

    @Override
    public void readProperties(PropertiesReader reader) {
        reader.read(this);
        this.action(reader.getString("action").orElse(""));
        for (var item : this) {
            if (item instanceof ISupplierBlock supplier)
                this.addBlock(supplier);
            if (item instanceof ISupportForm impl) {
                if (!Utils.isEmpty(impl.title()))
                    this.addColumn(impl.title());
            }
        }
    }

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        super.buildEditor(content, pageCode);

        EditorGrid grid = new EditorGrid(content, this);
        grid.addColumn("栏位", "cloumn", 20);
        grid.build(pageCode);

        // 显示所有可以加入的组件
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
                                                <span><input type="checkbox" name="components" value="${class},${field},${title}"></span>
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

        List<Field> fields = new ArrayList<>();
        Optional<ISupplierDataRow> optDataRow = this.dataRow.target();
        if (optDataRow.isPresent() && optDataRow.get() instanceof ISupplierFields supperli)
            fields.addAll(supperli.fields(ISupplierFields.BodyOutFields));

        Optional<ISupplierFields> optSvr = binders.findOwner(ISupplierFields.class);
        if (optSvr.isPresent())
            fields.addAll(optSvr.get().fields(ISupplierFields.HeadInFields));
        for (Field field : fields) {
            if (dataSet.locate("field", field.getName()))
                continue;
            String title = field.getName();
            Column column = field.getAnnotation(Column.class);
            if (column == null)
                continue;
            if (!Utils.isEmpty(column.name()))
                title = column.name();
            String classCode = FormStringField.class.getSimpleName();
            if (field.getType() == Boolean.class || field.getType() == boolean.class)
                classCode = FormBooleanField.class.getSimpleName();
            else if (field.getType() == Integer.class || field.getType() == int.class || field.getType() == Double.class
                    || field.getType() == double.class || field.getType().isEnum())
                classCode = FormNumberField.class.getSimpleName();
            dataSet.append()
                    .setValue("field", field.getName())
                    .setValue("title", title)
                    .setValue("class", classCode)
                    .setValue("check", false);
        }
    }

    @Override
    public void saveEditor(RequestReader reader) {
        reader.saveProperties(this);
        // 对栏位进行排序
        reader.sortComponent(this);
        // 批量添加组件
        batchAppendComponents();
        // 处理移除组件
        var item2 = reader.removeComponent(this);
        if (item2 != null)
            this.canvas().sendMessage(this, SsrMessage.removeComponent, item2, this.dataRow.targetId());
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
            String title = componentProperties[2];
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
            if (item instanceof ISupportForm formField) {
                formField.title(title);
                formField.field(field);
            }
            this.canvas().sendMessage(this, SsrMessage.appendComponent, item, this.dataRow.targetId());
        }
    }

    @Override
    public String getIdPrefix() {
        return "form";
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitRequest:
            if (msgData instanceof HttpServletRequest request)
                this.request = request;
            break;
        case SsrMessage.InitBinder:
            this.dataRow.init();
            break;
        case SsrMessage.InitProperties:
        case SsrMessage.RefreshProperties:
            if (Utils.isEmpty(this.dataRow.targetId())) {
                log.warn("{} 没有绑定数据源", this.getId());
                break;
            }
            var target = this.dataRow.target();
            if (target.isPresent())
                this.template.dataRow(target.get().dataRow());
            else
                log.warn("{} 绑定的数据源 {} 找不到", this.getId(), this.dataRow.targetId());
            break;
        case SsrMessage.InitContent:
            if (request != null) {
                if (this.readAll(request, "submit"))
                    this.canvas().sendMessage(this, SsrMessage.AfterSubmit, null, null);
            } else {
                log.error("request 为空，无法执行");
            }
            break;
        case SsrMessage.RenameFieldCode:
            if (msgData instanceof String newField)
                this.dataRow.sendMessage(SsrMessage.UpdateFieldCode, newField);
            break;
        }
    }

    @Override
    public Binders binders() {
        return binders;
    }

    @Override
    public ObjectNode properties() {
        var properties = super.properties();
        if (!properties.has("_width"))
            properties.put("_width", 0);
        if (!properties.has("_height"))
            properties.put("_height", 0);
        return properties;
    }

}
