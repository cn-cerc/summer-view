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
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IPage;
import cn.cerc.mis.other.MemoryBuffer;
import cn.cerc.ui.core.UIComponent;

public class UISsrForm extends UIComponent implements SsrComponentImpl {
    private static final Logger log = LoggerFactory.getLogger(UISsrForm.class);
    private SsrTemplate template;
    private List<String> fields;
    public static final String FormBegin = "form.begin";
    public static final String FormEnd = "form.end";
    public static final String FormStart = "formStart";
    private Map<String, Consumer<SsrBlockImpl>> onGetHtml = new HashMap<>();
    private MemoryBuffer buffer;

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
        if (this.fields == null)
            this.fields = this.dataRow().fields().names();

        getBlock(SsrTemplate.BeginFlag).ifPresent(template -> html.print(template.getHtml()));

        var top = getBlock(FormBegin, getDefault_FormBegin()).get();
        if (this.template.id() != null)
            top.option(SsrOptionImpl.TemplateId, this.template.id());
        html.print(top.getHtml());

        for (var field : fields) {
            var block = getBlock(field,
                    () -> new SsrBlock(
                            String.format("%s: <input type=\"text\" name=\"%s\" value=\"${%s}\">", field, field, field))
                            .setTemplate(template));
            if (block.isPresent()) {
                this.onGetHtml.forEach((key, value) -> {
                    if (key.equals(field))
                        value.accept(block.get().setId(field));
                });
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

    @Override
    public void onGetHtml(String field, Consumer<SsrBlockImpl> consumer) {
        this.onGetHtml.put(field, consumer);
    }

    private Supplier<SsrBlockImpl> getDefault_FormBegin() {
        var action = this.template().option("action").orElse("");
        return () -> {
            var ssr = new SsrBlock(String.format("<form method='post' action='%s' role='${role}'>${callback(%s)}<ul>",
                    action, UISsrForm.FormStart)).setTemplate(template);
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
            block.setId(id);
        else
            log.error("表单模版中缺失定义：{}", id);
        return Optional.ofNullable(block);
    }

    @Override
    public void addField(String... fields) {
        if (this.fields == null)
            this.fields = new ArrayList<>();
        for (var field : fields) {
            if (Utils.isEmpty(field))
                throw new RuntimeException("field 不允许为空");
            if (!this.fields.contains(field))
                this.fields.add(field);
        }
    }

    @Deprecated
    public UISsrForm addField(String id, Consumer<SsrBlockImpl> onGetHtml) {
        this.addField(id);
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
        return fields;
    }

    public List<String> fields() {
        return fields;
    }

    @Deprecated
    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    /**
     * 请改使用 buffer 函数
     * 
     * @param buffer
     */
    public void setBuffer(MemoryBuffer buffer) {
        buffer(buffer);
    }

    public UISsrForm buffer(MemoryBuffer buffer) {
        this.buffer = buffer;
        return this;
    }

    public boolean readAll(HttpServletRequest request, String submitId) {
        if (dataRow() == null)
            this.dataRow(new DataRow());
        boolean submit = request.getParameter(submitId) != null;
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

    @Override
    public DataSet getDefaultOptions() {
        return getDefaultOptions(template.id());
    }

    public DataSet getDefaultOptions(String template_id) {
        DataSet ds = new DataSet();
        for (var ssr : template) {
            var option = ssr.option(SsrOptionImpl.Display);
            if (option.isPresent() && !Utils.isEmpty(ssr.id()))
                ds.append().setValue("column_name_", ssr.id()).setValue("option_", option.get());
        }
        ds.head().setValue("template_id_", template_id);
        return ds;
    }

    @Override
    public void setConfig(DataSet configs) {
        configs.forEach(item -> {
            if (item.getEnum("option_", TemplateConfigOptionEnum.class) != TemplateConfigOptionEnum.不显示)
                addField(item.getString("column_name_"));
        });
    }

    public SsrFormStyleDefault createDefaultStyle() {
        return new SsrFormStyleDefault();
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

    public UISsrForm setTemplateId(String id) {
        template.id(id);
        return this;
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

}
