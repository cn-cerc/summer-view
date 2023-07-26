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
    private Map<String, Consumer<SsrTemplateImpl>> onGetHtml = new HashMap<>();
    private MemoryBuffer buff;

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
        if (page != null) {
            for (var ssr : define)
                ssr.setOption("isPhone", "" + page.getForm().getClient().isPhone());
        }
        return this;
    }

    public DataRow getDataRow() {
        return define.getDataRow();
    }

    public UISsrForm setDataRow(DataRow dataRow) {
        this.define.setDataRow(dataRow);
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
        top.setOption("templateId", this.define.id());
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
        var action = this.getDefine().getOption("action").orElse("");
        return () -> new SsrTemplate(String.format("<form method='post' action='%s'><ul>", action)).setDefine(define);
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
    public void addField(String... field) {
        if (fields == null)
            fields = new ArrayList<>();
        for (var item : field)
            fields.add(item);
    }

    @Deprecated
    public UISsrForm addField(String id, Consumer<SsrTemplateImpl> onGetHtml) {
        this.addField(id);
        this.onGetHtml.put(id, onGetHtml);
        return this;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void setBuffer(MemoryBuffer buff) {
        this.buff = buff;
    }

    public boolean readAll(HttpServletRequest request, String submitId) {
        boolean submit = request.getParameter(submitId) != null;
        for (var ssr : this.define) {
            ssr.getOption("fields").ifPresent(fields -> {
                for (var field : fields.split(",")) {
                    String val = request.getParameter(field);
                    updateValue(field, val, submit);
                }
            });
        }
        return submit;
    }

    private void updateValue(String field, String val, boolean submit) {
        if (submit) {
            getDataRow().setValue(field, val == null ? "" : val);
            if (buff != null) {
                buff.setValue(field, val);
            }
        } else {
            if (val != null) {
                getDataRow().setValue(field, val);
            } else if (buff != null && !buff.isNull() && buff.getRecord().exists(field)) {
                getDataRow().setValue(field, buff.getString(field));
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
            var option = ssr.getOption("option");
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

    public SsrFormStyleImpl createDefaultStyle() {
        return new SsrFormStyleDefault();
    }

    /**
     * 请改使用 getTemplate 或 addTemplate
     * 
     * @return
     */
    @Override
    public SsrDefine getDefine() {
        return this.define;
    }

    public UISsrForm setAction(String action) {
        setOption("action", action);
        return this;
    }

    public UISsrForm setTemplateId(String id) {
        define.setId(id);
        return this;
    }

}
