package cn.cerc.ui.style;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UITemplateGrid extends UIComponent {
    private static final Logger log = LoggerFactory.getLogger(UITemplateGrid.class);
    private DataSet dataSet;
    private SsrDefine define;
    private List<String> fields;
    private Consumer<SsrTemplateImpl> onGetBody;
    private Consumer<SsrTemplateImpl> onGetHead;
    // 表样式 id
    public static final String TableBegin = "table.begin";
    public static final String TableEnd = "table.end";
    // 表头样式id
    public static final String HeadBegin = "head.begin";
    public static final String HeadEnd = "head.end";
    // 表身样式id
    public static final String BodyBegin = "body.begin";
    public static final String BodyEnd = "body.end";

    public UITemplateGrid(UIComponent owner) {
        super(owner);
    }

    public UITemplateGrid(UIComponent owner, String templateText) {
        super(owner);
        define = new SsrDefine(templateText);
    }

    public UITemplateGrid(UIComponent owner, Class<?> class1, String id) {
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
        this.getComponents().clear();

        if (this.fields == null)
            this.fields = this.dataSet.fields().names();

        if (define.items().containsKey(SsrDefine.TopFlag))
            addBlock(SsrDefine.TopFlag, null).ifPresent(value -> html.print(value.getHtml()));

        addBlock(TableBegin, getDefault_TableBegin()).ifPresent(value -> html.print(value.getHtml()));

        // 输出标题
        addBlock(HeadBegin, getDefault_HeadBegin()).ifPresent(value -> html.print(value.getHtml()));
        for (var field : fields) {
            var block = addBlock("head." + field, getDefault_HeadCell(field));
            if (block.isPresent()) {
                if (onGetHead != null) {
                    block.get().setId(field);
                    onGetHead.accept(block.get());
                }
            }
            block.ifPresent(value -> html.print(value.getHtml()));
        }
        addBlock(HeadEnd, () -> new SsrTemplate("</tr>")).ifPresent(value -> html.print(value.getHtml()));

        // 输出内容
        if (dataSet.size() > 0) {
            var save_rec = dataSet.recNo();
            try {
                dataSet.first();
                while (dataSet.fetch()) {
                    addBlock(BodyBegin, getDefault_BodyBegin()).ifPresent(value -> html.print(value.getHtml()));
                    for (var field : fields) {
                        var block = addBlock("body." + field, getDefault_BodyCell(field));
                        if (block.isPresent()) {
                            if (onGetBody != null) {
                                block.get().setId(field);
                                onGetBody.accept(block.get());
                            }
                        }
                        block.ifPresent(value -> html.print(value.getHtml()));
                    }
                    addBlock(BodyEnd, () -> new SsrTemplate("</tr>")).ifPresent(value -> html.print(value.getHtml()));
                }
            } finally {
                dataSet.setRecNo(save_rec);
            }
        }

        addBlock(TableEnd, () -> new SsrTemplate("</table>")).ifPresent(value -> html.print(value.getHtml()));
    }

    private Optional<SsrTemplateImpl> addBlock(String id, Supplier<SsrTemplateImpl> supplier) {
        var item = define.get(id);
        SsrTemplateImpl template = item.orElse(null);
        if (template == null && supplier != null) {
            template = supplier.get();
            if (template != null)
                define.items().put(id, template);
        }
        if (template != null) {
            template.setId(id);
            template.setDataSet(dataSet);
        } else
            log.error("表格模版中缺失定义：{}", id);
        return Optional.ofNullable(template);
    }

    public void onGetHead(Consumer<SsrTemplateImpl> onAddItem) {
        this.onGetHead = onAddItem;
    }

    public void onGetBody(Consumer<SsrTemplateImpl> onAddColumn) {
        this.onGetBody = onAddColumn;
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

    public UITemplateGrid putDefine(String id, String templateText) {
        this.define.items().put(id, new SsrTemplate(templateText));
        return this;
    }

    public UITemplateGrid putHead(String field, String templateText) {
        return this.putDefine("head." + field, templateText);
    }

    public UITemplateGrid putBody(String field, String templateText) {
        return this.putDefine("body." + field, templateText);
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
        return () -> {
            var def = dataSet.fields(field);
            var templateText = String.format("<th>%s</th>", Utils.isEmpty(def.name()) ? def.code() : def.name());
            return new SsrTemplate(templateText);
        };
    }

    /**
     * 
     * @param field
     * @return 返回默认的表身单元格样式
     */
    private Supplier<SsrTemplateImpl> getDefault_BodyCell(String field) {
        return () -> new SsrTemplate(String.format("<td>${%s}</td>", field));
    }

}
