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
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IPage;
import cn.cerc.mis.other.MemoryBuffer;
import cn.cerc.ui.core.UIComponent;

public class UISsrForm extends UIComponent implements SsrComponentImpl {
    private static final Logger log = LoggerFactory.getLogger(UISsrForm.class);
    private SsrDefine define;
    private DataRow dataRow;
    private List<String> fields;
    public static final String FormBegin = "form.begin";
    public static final String FormEnd = "form.end";
    private Map<String, Consumer<SsrTemplateImpl>> onGetHtml = new HashMap<>();
    private boolean strict = true;
    private MemoryBuffer buff;

    public UISsrForm(UIComponent owner) {
        super(owner);
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
        return dataRow;
    }

    public void setDataRow(DataRow dataRow) {
        this.dataRow = dataRow;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.dataRow == null) {
            log.error("dataRow is null");
            return;
        }
        if (this.fields == null)
            this.fields = this.dataRow.fields().names();

        addBlock(SsrDefine.BeginFlag).ifPresent(value -> html.print(value.getHtml()));

        var top = addBlock(FormBegin, getDefault_FormBegin()).get();
        top.setOption("templateId", this.define.id());
        html.print(top.getHtml());

        for (var field : fields) {
            var block = addBlock(field, () -> new SsrTemplate(
                    String.format("%s: <input type=\"text\" name=\"%s\" value=\"${%s}\">", field, field, field)));
            if (block.isPresent()) {
                this.onGetHtml.forEach((key, value) -> {
                    if (key.equals(field))
                        value.accept(block.get().setId(field));
                });
            }
            block.ifPresent(value -> html.print(value.getHtml()));
        }
        addBlock(FormEnd, () -> new SsrTemplate("</form>")).ifPresent(value -> html.print(value.getHtml()));
        addBlock(SsrDefine.EndFlag).ifPresent(value -> html.print(value.getHtml()));
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

    public void onGetHtml(String field, Consumer<SsrTemplateImpl> consumer) {
        this.onGetHtml.put(field, consumer);
    }

    private Supplier<SsrTemplateImpl> getDefault_FormBegin() {
        return () -> new SsrTemplate("<form method='post'>");
    }

    private Optional<SsrTemplateImpl> addBlock(String id) {
        SsrTemplateImpl template = define.get(id).orElse(null);
        if (template != null) {
            template.setId(id);
            template.setDataRow(dataRow);
        }
        return Optional.ofNullable(template);
    }

    private Optional<SsrTemplateImpl> addBlock(String id, Supplier<SsrTemplateImpl> supplier) {
        SsrTemplateImpl template = define.getOrAdd(id, supplier).orElse(null);
        if (template != null) {
            template.setId(id);
            template.setDataRow(dataRow);
        } else
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

    public SsrDefine getDefine() {
        return define;
    }

    public boolean isStrict() {
        return strict;
    }

    public UISsrForm setStrict(boolean strict) {
        this.strict = strict;
        for (var block : define.items().values())
            block.setStrict(strict);
        return this;
    }

    public void setBuffer(MemoryBuffer buff) {
        this.buff = buff;
    }

    public boolean readAll(HttpServletRequest request, String submitId) {
        boolean submit = request.getParameter(submitId) != null;
        for (var ssr : define) {
            var option = ssr.getOption("fields");
            if (option.isPresent()) {
                var fields = option.get();
                for (var field : fields.split(",")) {
                    String val = request.getParameter(field);
                    updateValue(field, val, submit);
                }
            }
        }
        return true;
    }

    private void updateValue(String field, String val, boolean submit) {
        if (submit) {
            dataRow.setValue(field, val == null ? "" : val);
            if (buff != null) {
                buff.setValue(field, val);
            }
        } else {
            if (val != null) {
                dataRow.setValue(field, val);
            } else if (buff != null && !buff.isNull() && buff.getRecord().exists(field)) {
                dataRow.setValue(field, buff.getString(field));
            }
        }
    }

    @Override
    public DataSet getDefaultOptions() {
        DataSet ds = new DataSet();
        for (var ssr : define) {
            var option = ssr.getOption("option");
            if (option.isPresent()) {
                ds.append().setValue("column_name_", ssr.id()).setValue("option_", option.get());
            }
        }
        ds.head().setValue("template_id_", define.id());
        return ds;
    }

    @Override
    public void setConfig(DataSet configs) {
        configs.forEach(item -> {
            if (item.getEnum("option_", TemplateConfigOptionEnum.class) != TemplateConfigOptionEnum.不显示)
                addField(item.getString("column_name_"));
        });
    }

}
