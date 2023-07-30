package cn.cerc.ui.style;

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
    private SsrDefine define;
    private List<String> fields;
    public static final String FormBegin = "form.begin";
    public static final String FormEnd = "form.end";
    public static final String FormStart = "formStart";
    private Map<String, Consumer<SsrTemplateImpl>> onGetHtml = new HashMap<>();
    private MemoryBuffer buffer;

    public UISsrForm(UIComponent owner) {
        super(owner);
        define = new SsrDefine("");
        init(findOwner(IPage.class));
    }

    public UISsrForm(UIComponent owner, String templateText) {
        super(owner);
        define = new SsrDefine(templateText);
        init(findOwner(IPage.class));
    }

    public UISsrForm(UIComponent owner, Class<?> class1, String id) {
        super(owner);
        define = new SsrDefine(class1, id);
        init(findOwner(IPage.class));
    }

    private UISsrForm init(IPage page) {
        this.setId("form1");
        if (page != null) {
            for (var ssr : define)
                ssr.option(SsrOptionImpl.Phone, "" + page.getForm().getClient().isPhone());
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
        return define.dataRow();
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
        this.define.dataRow(dataRow);
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.getDataRow() == null) {
            log.error("dataRow is null");
            return;
        }
        if (this.fields == null)
            this.fields = this.getDataRow().fields().names();

        getTemplate(SsrDefine.BeginFlag).ifPresent(template -> html.print(template.getHtml()));

        var top = getTemplate(FormBegin, getDefault_FormBegin()).get();
        if (this.define.id() != null)
            top.option(SsrOptionImpl.TemplateId, this.define.id());
        html.print(top.getHtml());

        for (var field : fields) {
            var block = getTemplate(field,
                    () -> new SsrTemplate(
                            String.format("%s: <input type=\"text\" name=\"%s\" value=\"${%s}\">", field, field, field))
                            .setDefine(define));
            if (block.isPresent()) {
                this.onGetHtml.forEach((key, value) -> {
                    if (key.equals(field))
                        value.accept(block.get().setId(field));
                });
            }
            block.ifPresent(value -> html.print(value.getHtml()));
        }
        getTemplate(FormEnd, () -> new SsrTemplate("</ul></form>").setDefine(define))
                .ifPresent(value -> html.print(value.getHtml()));
        getTemplate(SsrDefine.EndFlag).ifPresent(template -> html.print(template.getHtml()));
    }

    /**
     * 请改使用 onGetHtml
     * 
     * @param field
     * @param consumer
     */
    @Deprecated
    public void addGetItem(String field, Consumer<SsrTemplateImpl> consumer) {
        this.onGetHtml(field, consumer);
    }

    @Override
    public void onGetHtml(String field, Consumer<SsrTemplateImpl> consumer) {
        this.onGetHtml.put(field, consumer);
    }

    private Supplier<SsrTemplateImpl> getDefault_FormBegin() {
        var action = this.getDefine().option("action").orElse("");
        return () -> {
            var ssr = new SsrTemplate(String.format(
                    "<form method='post' action='%s' role='${role}'>${callback(%s)}<ul>", action, UISsrForm.FormStart))
                    .setDefine(define);
            ssr.onCallback(UISsrForm.FormStart, () -> {
                var formFirst = this.getTemplate(UISsrForm.FormStart);
                formFirst.ifPresent(template -> {
                    if (this.define.id() != null)
                        template.option(SsrOptionImpl.TemplateId, this.define.id());
                });
                return formFirst.isPresent() ? formFirst.get().getHtml() : "";
            });
            ssr.option("role", "search");
            return ssr;
        };
    }

    private Optional<SsrTemplateImpl> getTemplate(String id, Supplier<SsrTemplateImpl> supplier) {
        SsrTemplateImpl template = define.getOrAdd(id, supplier).orElse(null);
        if (template != null)
            template.setId(id);
        else
            log.error("表单模版中缺失定义：{}", id);
        return Optional.ofNullable(template);
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
    public UISsrForm addField(String id, Consumer<SsrTemplateImpl> onGetHtml) {
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
        for (var ssr : this.define) {
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
        return getDefaultOptions(define.id());
    }

    public DataSet getDefaultOptions(String template_id) {
        DataSet ds = new DataSet();
        for (var ssr : define) {
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

    /**
     * 请改使用 define 函数
     * 
     * @return
     */
    @Override
    @Deprecated
    public SsrDefine getDefine() {
        return this.define();
    }

    public SsrDefine define() {
        return this.define;
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
        define.id(id);
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

    public UISsrForm role(String role) {
        this.option("role", role);
        return this;
    }

}
