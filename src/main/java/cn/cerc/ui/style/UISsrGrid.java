package cn.cerc.ui.style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

/**
 * 第3代 SSR UI表格
 *
 */
public class UISsrGrid extends UIComponent implements SsrComponentImpl, IGridStyle {
    private static final Logger log = LoggerFactory.getLogger(UISsrGrid.class);
    private SsrDefine define;
    private List<String> fields;
    private Map<String, Consumer<SsrTemplateImpl>> onGetBodyHtml = new HashMap<>();
    private Map<String, Consumer<SsrTemplateImpl>> onGetHeadHtml = new HashMap<>();
    // 表样式 id
    public static final String TableBegin = "table.begin";
    public static final String TableEnd = "table.end";
    // 表头样式id
    public static final String HeadBegin = "head.begin";
    public static final String HeadEnd = "head.end";
    // 表身样式id
    public static final String BodyBegin = "body.begin";
    public static final String BodyEnd = "body.end";
    private String emptyText;

    public UISsrGrid(UIComponent owner) {
        super(owner);
        define = new SsrDefine("");
    }

    public UISsrGrid(UIComponent owner, String templateText) {
        super(owner);
        define = new SsrDefine(templateText);
    }

    public UISsrGrid(UIComponent owner, Class<?> class1, String id) {
        super(owner);
        define = new SsrDefine(class1, id);
    }

    public DataSet getDataSet() {
        return define.getDataSet();
    }

    public UISsrGrid setDataSet(DataSet dataSet) {
        define.setDataSet(dataSet);
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.getDataSet() == null) {
            log.error("dataSet is null");
            return;
        }
        if (this.fields == null) {
            if (this.emptyText == null)
                html.print(String.format("dataSet.size=%d, fields is null, emptyText is null", getDataSet().size()));
            else
                html.print(this.emptyText);
            return;
        }

        getTemplate(SsrDefine.BeginFlag).ifPresent(template -> html.print(template.getHtml()));
        getTemplate(TableBegin, getDefault_TableBegin()).ifPresent(value -> html.print(value.getHtml()));

        // 输出标题
        getTemplate(HeadBegin, getDefault_HeadBegin()).ifPresent(value -> html.print(value.getHtml()));
        for (var field : fields) {
            var block = getTemplate("head." + field, getDefault_HeadCell(field));
            if (block.isPresent()) {
                block.get().option(SsrOptionImpl.TemplateId, this.define.id());
                this.onGetHeadHtml.forEach((key, value) -> {
                    if (key.equals(field))
                        value.accept(block.get().setId(field));
                });
            }
            block.ifPresent(value -> html.print(value.getHtml()));
        }
        getTemplate(HeadEnd, () -> new SsrTemplate("</tr>").setDefine(define))
                .ifPresent(value -> html.print(value.getHtml()));

        // 输出内容
        if (getDataSet().size() > 0) {
            var save_rec = getDataSet().recNo();
            try {
                getDataSet().first();
                while (getDataSet().fetch()) {
                    getTemplate(BodyBegin, getDefault_BodyBegin()).ifPresent(value -> html.print(value.getHtml()));
                    for (var field : fields) {
                        var block = getTemplate("body." + field, getDefault_BodyCell(field));
                        if (block.isPresent()) {
                            this.onGetBodyHtml.forEach((key, value) -> {
                                if (key.equals(field))
                                    value.accept(block.get().setId(field));
                            });
                        }
                        block.ifPresent(value -> html.print(value.getHtml()));
                    }
                    getTemplate(BodyEnd, () -> new SsrTemplate("</tr>").setDefine(define))
                            .ifPresent(value -> html.print(value.getHtml()));
                }
            } finally {
                getDataSet().setRecNo(save_rec);
            }
        }

        getTemplate(TableEnd, () -> new SsrTemplate("</table></div>").setDefine(define))
                .ifPresent(value -> html.print(value.getHtml()));
        getTemplate(SsrDefine.EndFlag).ifPresent(template -> html.print(template.getHtml()));

    }

    private Optional<SsrTemplateImpl> getTemplate(String id, Supplier<SsrTemplateImpl> supplier) {
        SsrTemplateImpl template = define.getOrAdd(id, supplier).orElse(null);
        if (template != null)
            template.setId(id);
        else
            log.error("表格模版中缺失定义：{}", id);
        return Optional.ofNullable(template);
    }

    /**
     * 请改使用 onGetHeadHtml
     * 
     * @param field
     * @param consumer
     */
    @Deprecated
    public void addGetHead(String field, Consumer<SsrTemplateImpl> consumer) {
        this.onGetHeadHtml(field, consumer);
    }

    public void onGetHeadHtml(String field, Consumer<SsrTemplateImpl> consumer) {
        this.onGetHeadHtml.put(field, consumer);
    }

    /**
     * 请改使用 onGetBody
     * 
     * @param field
     * @param consumer
     */
    @Deprecated
    public void addGetBody(String field, Consumer<SsrTemplateImpl> consumer) {
        this.onGetBodyHtml(field, consumer);
    }

    public void onGetBodyHtml(String field, Consumer<SsrTemplateImpl> consumer) {
        this.onGetBodyHtml.put(field, consumer);
    }

    @Override
    public void onGetHtml(String field, Consumer<SsrTemplateImpl> consumer) {
        if (field.startsWith("head."))
            this.onGetHeadHtml(field.substring(5, field.length()), consumer);
        else if (field.startsWith("body."))
            this.onGetBodyHtml(field.substring(5, field.length()), consumer);
        else
            throw new RuntimeException("只支持以head.或body.开头的事件");
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    @Override
    public SsrDefine getDefine() {
        return define;
    }

    /**
     * 请改使用 addTemplate
     * 
     * @param id
     * @param templateText
     * @return
     */
    @Deprecated
    public UISsrGrid putDefine(String id, String templateText) {
        addTemplate(id, templateText);
        return this;
    }

    /**
     * 
     * @return 返回默认的表头样式
     */
    private Supplier<SsrTemplateImpl> getDefault_TableBegin() {
        return () -> new SsrTemplate("<div id='grid' class='scrollArea'><table class='dbgrid'>").setDefine(define);
    }

    /**
     * 
     * @return 返回表头行
     */
    private Supplier<SsrTemplateImpl> getDefault_HeadBegin() {
        return () -> new SsrTemplate("<tr>").setDefine(define);
    }

    /**
     * 
     * @return 返回表身行
     */
    private Supplier<SsrTemplateImpl> getDefault_BodyBegin() {
        return () -> new SsrTemplate("<tr>").setDefine(define);
    }

    /**
     * 
     * @param field
     * @return 返回默认的表头单元格样式
     */
    private Supplier<SsrTemplateImpl> getDefault_HeadCell(String field) {
        return () -> new SsrTemplate(String.format("<th>%s</th>", field)).setDefine(define);
    }

    /**
     * 
     * @param field
     * @return 返回默认的表身单元格样式
     */
    private Supplier<SsrTemplateImpl> getDefault_BodyCell(String field) {
        return () -> new SsrTemplate(String.format("<td>${%s}</td>", field)).setDefine(define);
    }

    @Override
    public void addField(String... field) {
        if (fields == null)
            fields = new ArrayList<>();
        for (var item : field)
            fields.add(item);
    }

    @Override
    public DataSet getDefaultOptions() {
        DataSet ds = new DataSet();
        for (var ssr : define) {
            var option = ssr.option(SsrOptionImpl.Display);
            String id = ssr.id();
            if (option.isPresent()) {
                if (id.startsWith("body.") || id.startsWith("head."))
                    id = id.substring(5, id.length());
                if (!ds.locate("column_name_", id))
                    ds.append().setValue("column_name_", id).setValue("option_", option.get());
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

    @Override
    public Optional<SsrTemplateImpl> getTemplate(String templateId) {
        return define.get(templateId);
    }

    public String getEmptyText() {
        return emptyText;
    }

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }

    public SsrGridStyleDefault createDefaultStyle() {
        return new SsrGridStyleDefault();
    }

}
