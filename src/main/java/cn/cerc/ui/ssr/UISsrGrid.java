package cn.cerc.ui.ssr;

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
import cn.cerc.ui.style.IGridStyle;

/**
 * 第3代 SSR UI表格
 *
 */
public class UISsrGrid extends UIComponent implements SsrComponentImpl, IGridStyle {
    private static final Logger log = LoggerFactory.getLogger(UISsrGrid.class);
    private SsrTemplate template;
    private List<String> fields;
    private Map<String, Consumer<SsrBlockImpl>> onGetBodyHtml = new HashMap<>();
    private Map<String, Consumer<SsrBlockImpl>> onGetHeadHtml = new HashMap<>();
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
        template = new SsrTemplate("");
    }

    public UISsrGrid(UIComponent owner, String templateText) {
        super(owner);
        template = new SsrTemplate(templateText);
    }

    public UISsrGrid(UIComponent owner, Class<?> class1, String id) {
        super(owner);
        template = new SsrTemplate(class1, id);
    }

    public DataSet getDataSet() {
        return template.dataSet();
    }

    public UISsrGrid setDataSet(DataSet dataSet) {
        template.dataSet(dataSet);
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

        getBlock(SsrTemplate.BeginFlag).ifPresent(template -> html.print(template.getHtml()));
        getTemplate(TableBegin, getDefault_TableBegin()).ifPresent(value -> html.print(value.getHtml()));

        // 输出标题
        getTemplate(HeadBegin, getDefault_HeadBegin()).ifPresent(value -> html.print(value.getHtml()));
        for (var field : fields) {
            var block = getTemplate("head." + field, getDefault_HeadCell(field));
            if (block.isPresent()) {
                block.get().option(SsrOptionImpl.TemplateId, this.template.id());
                this.onGetHeadHtml.forEach((key, value) -> {
                    if (key.equals(field))
                        value.accept(block.get().setId(field));
                });
            }
            block.ifPresent(value -> html.print(value.getHtml()));
        }
        getTemplate(HeadEnd, () -> new SsrBlock("</tr>").setTemplate(template))
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
                    getTemplate(BodyEnd, () -> new SsrBlock("</tr>").setTemplate(template))
                            .ifPresent(value -> html.print(value.getHtml()));
                }
            } finally {
                getDataSet().setRecNo(save_rec);
            }
        }

        getTemplate(TableEnd, () -> new SsrBlock("</table></div>").setTemplate(template))
                .ifPresent(value -> html.print(value.getHtml()));
        getBlock(SsrTemplate.EndFlag).ifPresent(template -> html.print(template.getHtml()));

    }

    private Optional<SsrBlockImpl> getTemplate(String id, Supplier<SsrBlockImpl> supplier) {
        SsrBlockImpl block = template.getOrAdd(id, supplier).orElse(null);
        if (block != null)
            block.setId(id);
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
    public void addGetHead(String field, Consumer<SsrBlockImpl> consumer) {
        this.onGetHeadHtml(field, consumer);
    }

    public void onGetHeadHtml(String field, Consumer<SsrBlockImpl> consumer) {
        this.onGetHeadHtml.put(field, consumer);
    }

    /**
     * 请改使用 onGetBody
     * 
     * @param field
     * @param consumer
     */
    @Deprecated
    public void addGetBody(String field, Consumer<SsrBlockImpl> consumer) {
        this.onGetBodyHtml(field, consumer);
    }

    public void onGetBodyHtml(String field, Consumer<SsrBlockImpl> consumer) {
        this.onGetBodyHtml.put(field, consumer);
    }

    @Override
    public void onGetHtml(String field, Consumer<SsrBlockImpl> consumer) {
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
    public UISsrGrid putDefine(String id, String templateText) {
        addBlock(id, templateText);
        return this;
    }

    /**
     * 
     * @return 返回默认的表头样式
     */
    private Supplier<SsrBlockImpl> getDefault_TableBegin() {
        return () -> new SsrBlock("<div id='grid' class='scrollArea'><table class='dbgrid'>").setTemplate(template);
    }

    /**
     * 
     * @return 返回表头行
     */
    private Supplier<SsrBlockImpl> getDefault_HeadBegin() {
        return () -> new SsrBlock("<tr>").setTemplate(template);
    }

    /**
     * 
     * @return 返回表身行
     */
    private Supplier<SsrBlockImpl> getDefault_BodyBegin() {
        return () -> new SsrBlock("<tr>").setTemplate(template);
    }

    /**
     * 
     * @param field
     * @return 返回默认的表头单元格样式
     */
    private Supplier<SsrBlockImpl> getDefault_HeadCell(String field) {
        return () -> new SsrBlock(String.format("<th>%s</th>", field)).setTemplate(template);
    }

    /**
     * 
     * @param field
     * @return 返回默认的表身单元格样式
     */
    private Supplier<SsrBlockImpl> getDefault_BodyCell(String field) {
        return () -> new SsrBlock(String.format("<td>${%s}</td>", field)).setTemplate(template);
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
        for (var ssr : template) {
            var option = ssr.option(SsrOptionImpl.Display);
            String id = ssr.id();
            if (option.isPresent()) {
                if (id.startsWith("body.") || id.startsWith("head."))
                    id = id.substring(5, id.length());
                if (!ds.locate("column_name_", id))
                    ds.append().setValue("column_name_", id).setValue("option_", option.get());
            }
        }
        ds.head().setValue("template_id_", template.id());
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
    public Optional<SsrBlockImpl> getBlock(String blockId) {
        return template.get(blockId);
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
    
    public UISsrGrid setTemplateId(String id) {
        template.id(id);
        return this;
    }
}
