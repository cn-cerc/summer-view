package cn.cerc.ui.ssr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.IHandle;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IPage;
import cn.cerc.mis.other.MemoryBuffer;
import cn.cerc.ui.core.UIComponent;

public class UISsrForm extends UIComponent implements SsrComponentImpl {
    private static final Logger log = LoggerFactory.getLogger(UISsrForm.class);
    private SsrTemplate template;
    private List<String> columns = new ArrayList<>();
    public static final String FormBegin = "form.begin";
    public static final String FormEnd = "form.end";
    public static final String FormStart = "formStart";
    private Map<String, Consumer<SsrBlockImpl>> onGetHtml = new HashMap<>();
    private MemoryBuffer buffer;
    private SsrFormStyleDefault defaultStle;

    public UISsrForm(UIComponent owner) {
        super(owner);
        template = new SsrTemplate("");
        init(findOwner(IPage.class));
    }

    public UISsrForm(UIComponent owner, String templateText) {
        super(owner);
        template = new SsrTemplate(templateText);
        init(findOwner(IPage.class));
    }

    public UISsrForm(UIComponent owner, Class<?> class1, String id) {
        super(owner);
        template = new SsrTemplate(class1, id);
        init(findOwner(IPage.class));
    }

    private UISsrForm init(IPage page) {
        this.setId("form1");
        if (page != null) {
            for (var block : template)
                block.option(SsrOptionImpl.Phone, "" + page.getForm().getClient().isPhone());
        }
        return this;
    }

    /**
     * 请改使用 dataRow 函数
     * 
     * @return
     */
    @Deprecated
    public DataRow getDataRow() {
        return dataRow();
    }

    public DataRow dataRow() {
        return template.dataRow();
    }

    /**
     * 请改使用 dataRow 函数
     * 
     * @param dataRow
     * @return
     */
    @Deprecated
    public UISsrForm setDataRow(DataRow dataRow) {
        dataRow(dataRow);
        return this;
    }

    public UISsrForm dataRow(DataRow dataRow) {
        this.template.dataRow(dataRow);
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.dataRow() == null) {
            log.error("dataRow is null");
            return;
        }
        if (this.columns == null)
            this.columns = this.dataRow().fields().names();

        getBlock(SsrTemplate.BeginFlag).ifPresent(template -> html.print(template.getHtml()));

        var top = getBlock(FormBegin, getDefault_FormBegin()).get();
        if (this.template.id() != null)
            top.option(SsrOptionImpl.TemplateId, this.template.id());
        html.print(top.getHtml());

        for (var field : columns) {
            var block = getBlock(field,
                    () -> new SsrBlock(
                            String.format("%s: <input type=\"text\" name=\"%s\" value=\"${%s}\">", field, field, field))
                            .setTemplate(template));
            if (block.isPresent()) {
                var value = onGetHtml.get(field);
                if (value != null)
                    value.accept(block.get().id(field));
            }
            block.ifPresent(value -> html.print(value.getHtml()));
        }
        getBlock(FormEnd, () -> new SsrBlock("</ul></form>").setTemplate(template))
                .ifPresent(value -> html.print(value.getHtml()));
        getBlock(SsrTemplate.EndFlag).ifPresent(template -> html.print(template.getHtml()));
    }

    /**
     * 请改使用 onGetHtml
     * 
     * @param field
     * @param consumer
     */
    @Deprecated
    public void addGetItem(String field, Consumer<SsrBlockImpl> consumer) {
        this.onGetHtml(field, consumer);
    }

    public void onGetHtml(String field, Consumer<SsrBlockImpl> consumer) {
        this.onGetHtml.put(field, consumer);
    }

    private Supplier<SsrBlockImpl> getDefault_FormBegin() {
        var action = this.template().option("action").orElse("");
        return () -> {
            var ssr = new SsrBlock(String.format("<form method='post' action='%s'%s role='${role}'>${callback(%s)}<ul>",
                    action, !Utils.isEmpty(getId()) ? " id='" + getId() + "'" : "", UISsrForm.FormStart))
                    .setTemplate(template);
            ssr.onCallback(UISsrForm.FormStart, () -> {
                var formFirst = this.getBlock(UISsrForm.FormStart);
                formFirst.ifPresent(template -> {
                    if (this.template.id() != null)
                        template.option(SsrOptionImpl.TemplateId, this.template.id());
                });
                return formFirst.isPresent() ? formFirst.get().getHtml() : "";
            });
            ssr.option("role", "search");
            return ssr;
        };
    }

    private Optional<SsrBlockImpl> getBlock(String id, Supplier<SsrBlockImpl> supplier) {
        SsrBlockImpl block = template.getOrAdd(id, supplier).orElse(null);
        if (block != null)
            block.id(id);
        else
            log.error("表单模版中缺失定义：{}", id);
        return Optional.ofNullable(block);
    }

    @Deprecated
    public UISsrForm addField(String id, Consumer<SsrBlockImpl> onGetHtml) {
        this.addColumn(id);
        this.onGetHtml.put(id, onGetHtml);
        return this;
    }

    /**
     * 请改使用 fields 函数
     * 
     * @return
     */
    @Deprecated
    public List<String> getFields() {
        return columns;
    }

    @Deprecated
    public List<String> fields() {
        return columns();
    }

    @Deprecated
    public void setFields(List<String> fields) {
        this.columns = fields;
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

    public UISsrForm buffer(MemoryBuffer buffer) {
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
                    String val = request.getParameter(field);
                    updateValue(field, val, submit);
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
            var option = ssr.option(SsrOptionImpl.Display);
            if (option.isPresent() && !Utils.isEmpty(ssr.id()))
                ds.append().setValue("column_name_", ssr.id()).setValue("option_", option.get());
        }
        ds.head().setValue("template_id_", template_id);
        return ds;
    }

    public void loadConfig(IHandle handle) {
        var context = Application.getContext();
        var bean = context.getBean(SsrConfigImpl.class);
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

    /**
     * 请改使用 action 函数
     * 
     * @param action
     * @return
     */
    @Deprecated
    public UISsrForm setAction(String action) {
        return action(action);
    }

    public UISsrForm action(String action) {
        option("action", action);
        return this;
    }

    /**
     * 请使用 templateId 函数
     * 
     * @param id
     * @return
     */
    @Deprecated
    public UISsrForm setTemplateId(String id) {
        template.id(id);
        return this;
    }

    public UISsrForm templateId(String id) {
        template.id(id);
        return this;
    }

    public String templateId() {
        return template.id();
    }

    public UISsrForm role(String role) {
        this.option("role", role);
        return this;
    }

    /**
     * 请改使用 role 函数
     * 
     * @return
     */
    @Deprecated
    public UISsrForm modify() {
        role("modify");
        return this;
    }

    @Deprecated
    public Optional<SsrBlockImpl> getTemplate(String blockId) {
        return this.getBlock(blockId);
    }

    @Deprecated
    public SsrBlockImpl addTemplate(SupplierBlockImpl supplier) {
        return addBlock(supplier);
    }

    @Deprecated
    public SsrTemplate getDefine() {
        return template();
    }

    @Deprecated
    public SsrTemplate define() {
        return template();
    }

    @Deprecated
    public SsrBlockImpl addTemplate(String id, String templateText) {
        return this.addBlock(id, templateText);
    }

    @Deprecated
    public void addField(String name) {
        this.addColumn(name);
    }

    @Deprecated
    public void addField(String... fields) {
        this.addColumn(fields);
    }

    @Override
    public List<String> columns() {
        return columns;
    }

}
