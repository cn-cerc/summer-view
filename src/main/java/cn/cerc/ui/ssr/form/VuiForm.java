package cn.cerc.ui.ssr.form;

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
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.ViewDisplay;
import cn.cerc.ui.ssr.chart.ISupportChart;
import cn.cerc.ui.ssr.core.AlignEnum;
import cn.cerc.ui.ssr.core.ISsrOption;
import cn.cerc.ui.ssr.core.ISsrTemplateConfig;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.PropertiesReader;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrTemplate;
import cn.cerc.ui.ssr.core.VuiBufferType;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.ISupportCanvas;
import cn.cerc.ui.ssr.page.IVuiEnvironment;
import cn.cerc.ui.ssr.page.VuiEnvironment;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.Binders;
import cn.cerc.ui.ssr.source.IBinders;
import cn.cerc.ui.ssr.source.ISupplierDataRow;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("表单")
@VuiCommonComponent
public class VuiForm extends VuiContainer<ISupportForm>
        implements ISsrBoard, ISupplierDataRow, ISupportCanvas, ISupportChart, IBinders {
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
    @Column
    String bufferKey = "";
    @Column(name = "动作(action)")
    String action = "";
    @Column
    Binder<ISupplierDataRow> dataRow = new Binder<>(this, ISupplierDataRow.class);
    @Column
    AlignEnum align = AlignEnum.None;
    @Column
    boolean enableConfig = true;
    private IHandle handle;
    private boolean submit;

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
        html.println("<script>$(function() { initForm('#%s') });</script>", getId());
        var top = getBlock(FormBegin, getDefault_FormBegin()).get();
        if (this.template.id() != null) {
            top.option(ISsrOption.TemplateId, this.template.id());
            if (this.getBlock(FormEnd).isPresent()) {
                this.getBlock(FormEnd).get().option(ISsrOption.TemplateId, this.template.id());
            }
        }
        html.print(top.html());

        writeContent(html);

        var formFirst = this.getBlock(VuiForm.FormStart);
        getBlock(FormEnd,
                () -> new SsrBlock(String.format("%s%s</form>", this.canvas() != null ? "" : "</ul>",
                        formFirst.isPresent() && isPhone() && this.canvas() == null ? formFirst.get().html() : ""))
                        .template(template))
                .ifPresent(value -> html.print(value.html()));
        getBlock(SsrTemplate.EndFlag).ifPresent(template -> html.print(template.html()));
    }

    public void writeContent(HtmlWriter html) {
        if (this.canvas() != null) {
            for (var component : this) {
                component.output(html);
            }
        } else {
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
                    html.print(new SsrBlock(String.format("%s: <input type=\"text\" name=\"%s\" value=\"${%s}\">",
                            column, column, column)).template(template).html());
                    log.error("找不到数据列: {}", column);
                }
            }
        }
    }

    public void onGetHtml(String field, Consumer<SsrBlock> consumer) {
        this.onGetHtml.put(field, consumer);
    }

    protected Supplier<SsrBlock> getDefault_FormBegin() {
        var action = this.template().option("action").orElse("");
        return () -> {
            var ssr = new SsrBlock(
                    String.format("<form method='post' action='%s'%s role='${role}' class='vuiForm'>${callback(%s)}%s",
                            action, !Utils.isEmpty(getId()) ? " id='" + getId() + "'" : "", VuiForm.FormStart,
                            this.canvas() != null ? "" : "<ul>"))
                    .template(template);
            ssr.onCallback(VuiForm.FormStart, () -> {
                var formFirst = this.getBlock(VuiForm.FormStart);
                formFirst.ifPresent(template -> {
                    if (this.template.id() != null)
                        template.option(ISsrOption.TemplateId, this.template.id());
                });
                return formFirst.isPresent() && !isPhone() && this.canvas() == null ? formFirst.get().html() : "";
            });
            ssr.option("role", Utils.isEmpty(this.getRole()) ? "search" : this.getRole());
            return ssr;
        };
    }

    protected Optional<SsrBlock> getBlock(String id, Supplier<SsrBlock> supplier) {
        SsrBlock block = template.getOrAdd(id, supplier).orElse(null);
        if (block != null)
            block.id(id);
        else
            log.error("表单模版中缺失定义：{}", id);
        return Optional.ofNullable(block);
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

        this.submit = request.getParameter(submitId) != null
                && (submitVal == null || submitVal.equals(request.getParameter(submitId)));

        for (String column : columns)
            updateValue(column, request);

        updateValue(VuiForm.FormStart, request);

        return submit;
    }

    private void updateValue(String column, HttpServletRequest request) {
        if (!columns.contains(column) && !VuiForm.FormStart.equals(column))
            return;

        var block = template().get(column);
        if (block.isEmpty())
            return;

        var fields1 = block.get().option("fields");
        if (fields1.isEmpty())
            return;

        var fields = fields1.get();
        for (var field : fields.split(",")) {
            if (!Utils.isEmpty(field)) {
                String val = request.getParameter(field);
                if (val != null)
                    val = val.trim();

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
        }
    }

    /**
     * 请改使用 loadConfig
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
        DataSet defaultDataSet = this.getDefaultOptions();
        if (defaultDataSet != null && !defaultDataSet.eof())
            for (var field : bean.getFields(handle, defaultDataSet))
                this.addColumn(field);
    }

    public void loadDefaultConfig() {
        this.getDefaultOptions().forEach(item -> {
            if (item.getEnum("option_", ViewDisplay.class) != ViewDisplay.默认隐藏)
                addColumn(item.getString("column_name_"));
        });
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
        if (this.canvas() == null) {
            for (var item : this) {
                if (item instanceof ISupplierBlock supplier)
                    this.addBlock(supplier);
                if (item instanceof ISupportField impl) {
                    if (!Utils.isEmpty(impl.title()))
                        this.addColumn(impl.title());
                }
            }
        }
    }

    @Override
    public Set<Class<? extends VuiComponent>> getChildren() {
        Set<Class<? extends VuiComponent>> set = super.getChildren();
        return set;
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
            if (item instanceof ISupportField formField) {
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
        case SsrMessage.InitHandle:
            if (msgData instanceof IHandle handle)
                this.handle = handle;
            break;
        case SsrMessage.InitBinder:
            this.dataRow.init();
            break;
        case SsrMessage.InitProperties:
            if (!Utils.isEmpty(bufferKey) && buffer == null) {
                SsrBlock block = new SsrBlock(bufferKey);
                block.option("CorpNo", handle.getCorpNo());
                block.option("UserCode", handle.getUserCode());
                buffer = new MemoryBuffer(VuiBufferType.VuiForm, block.html());
            }
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
                if (buffer != null)
                    buffer.post();
                binders().findOwner(VuiDataService.class)
                        .ifPresent(service -> service.onMessage(this, SsrMessage.InitContent, null, null));
                if (enableConfig) {
                    if (canvas().environment() instanceof VuiEnvironment environment) {
                        String pageCode = environment.getPageCode();
                        this.templateId(pageCode);
                        this.columns.clear();
                        this.loadConfig(handle);
                    }
                }
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
