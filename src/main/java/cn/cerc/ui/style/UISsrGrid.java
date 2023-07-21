package cn.cerc.ui.style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UISsrGrid extends UIComponent implements SsrComponentImpl {
    private static final Logger log = LoggerFactory.getLogger(UISsrGrid.class);
    private DataSet dataSet;
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

    public UISsrGrid(UIComponent owner) {
        super(owner);
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
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.dataSet == null) {
            log.error("dataSet is null");
            return;
        }

        if (this.fields == null)
            this.fields = this.dataSet.fields().names();

        getTemplate(SsrDefine.BeginFlag).ifPresent(template -> {
            template.setDataSet(dataSet);
            html.print(template.getHtml());
        });
        getTemplate(TableBegin, getDefault_TableBegin()).ifPresent(value -> html.print(value.getHtml()));

        // 输出标题
        getTemplate(HeadBegin, getDefault_HeadBegin()).ifPresent(value -> html.print(value.getHtml()));
        for (var field : fields) {
            var block = getTemplate("head." + field, getDefault_HeadCell(field));
            if (block.isPresent()) {
                block.get().setOption("templateId", this.define.id());
                this.onGetHeadHtml.forEach((key, value) -> {
                    if (key.equals(field))
                        value.accept(block.get().setId(field));
                });
            }
            block.ifPresent(value -> html.print(value.getHtml()));
        }
        getTemplate(HeadEnd, () -> new SsrTemplate("</tr>")).ifPresent(value -> html.print(value.getHtml()));

        // 输出内容
        if (dataSet.size() > 0) {
            var save_rec = dataSet.recNo();
            try {
                dataSet.first();
                while (dataSet.fetch()) {
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
                    getTemplate(BodyEnd, () -> new SsrTemplate("</tr>"))
                            .ifPresent(value -> html.print(value.getHtml()));
                }
            } finally {
                dataSet.setRecNo(save_rec);
            }
        }

        getTemplate(TableEnd, () -> new SsrTemplate("</table>")).ifPresent(value -> html.print(value.getHtml()));
        getTemplate(SsrDefine.EndFlag).ifPresent(template -> {
            template.setDataSet(dataSet);
            html.print(template.getHtml());
        });
    }

    private Optional<SsrTemplateImpl> getTemplate(String id, Supplier<SsrTemplateImpl> supplier) {
        SsrTemplateImpl template = define.getOrAdd(id, supplier).orElse(null);
        if (template != null) {
            template.setId(id);
            template.setDataSet(dataSet);
        } else
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

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

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
        return addTemplate(id, templateText);
    }

    public SsrDefaultGridStyle createDefaultStyle() {
        return new SsrDefaultGridStyle(this);
    }

    /**
     * 
     * @return 返回默认的表头样式
     */
    private Supplier<SsrTemplateImpl> getDefault_TableBegin() {
        return () -> new SsrTemplate("<table>");
    }

    /**
     * 
     * @return 返回表头行
     */
    private Supplier<SsrTemplateImpl> getDefault_HeadBegin() {
        return () -> new SsrTemplate("<tr>");
    }

    /**
     * 
     * @return 返回表身行
     */
    private Supplier<SsrTemplateImpl> getDefault_BodyBegin() {
        return () -> new SsrTemplate("<tr>");
    }

    /**
     * 
     * @param field
     * @return 返回默认的表头单元格样式
     */
    private Supplier<SsrTemplateImpl> getDefault_HeadCell(String field) {
        return () -> new SsrTemplate(String.format("<th>%s</th>", field));
    }

    /**
     * 
     * @param field
     * @return 返回默认的表身单元格样式
     */
    private Supplier<SsrTemplateImpl> getDefault_BodyCell(String field) {
        return () -> new SsrTemplate(String.format("<td>${%s}</td>", field));
    }

    @Override
    public void addField(String... field) {
        if (fields == null)
            fields = new ArrayList<>();
        for (var item : field)
            fields.add(item);
    }

    public void addField(SsrGridColumn column) {
        Objects.requireNonNull(column);
        if (fields == null)
            fields = new ArrayList<>();
        if (column != null) {
            fields.add(column.field());
            addTemplate("head." + column.field(), column.headStyle());
            addTemplate("body." + column.field(), column.bodyStyle());
        }
    }

    @Override
    public DataSet getDefaultOptions() {
        DataSet ds = new DataSet();
        for (var ssr : define) {
            var option = ssr.getOption("option");
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
    public UISsrGrid addTemplate(String id, String templateText) {
        this.define.items().put(id, new SsrTemplate(templateText));
        return this;
    }

    @Override
    public Optional<SsrTemplateImpl> getTemplate(String templateId) {
        return define.get(templateId);
    }

}
