package cn.cerc.ui.ssr;

import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UISsrChunk extends UIComponent implements SsrComponentImpl {
    private static final Logger log = LoggerFactory.getLogger(UISsrChunk.class);
    private SsrTemplate template;
    public static final String ListBegin = "list.begin";
    public static final String ListEnd = "list.end";

    public UISsrChunk(UIComponent owner) {
        super(owner);
        template = new SsrTemplate();
    }

    public UISsrChunk(UIComponent owner, String templateText) {
        super(owner);
        template = new SsrTemplate(templateText);
    }

    public UISsrChunk(UIComponent owner, Class<?> class1, String id) {
        super(owner);
        template = new SsrTemplate(class1, id);
    }

    public DataSet dataSet() {
        return template.dataSet();
    }

    public UISsrChunk dataSet(DataSet dataSet) {
        template.dataSet(dataSet);
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.dataSet() == null) {
            log.error("dataSet is null");
            return;
        }
        if (dataSet().size() > 0) {
            getBlock(SsrTemplate.BeginFlag, () -> new SsrBlock("<div role='chunkBox'>").setTemplate(template))
                    .ifPresent(template -> html.print(template.getHtml()));
            var save_rec = dataSet().recNo();
            try {
                dataSet().first();
                while (dataSet().fetch()) {
                    getBlock(ListBegin, () -> new SsrBlock("<ul role='chunkBoxItem'>").setTemplate(template))
                            .ifPresent(template -> html.print(template.getHtml()));
                    for (var component : getComponents()) {
                        if (component instanceof UISsrBoard board)
                            board.template(template);
                        component.output(html);
                    }
                    getBlock(ListEnd, () -> new SsrBlock("</ul>").setTemplate(template))
                            .ifPresent(template -> html.print(template.getHtml()));
                }
            } finally {
                dataSet().setRecNo(save_rec);
            }
            getBlock(SsrTemplate.EndFlag, () -> new SsrBlock("</div>").setTemplate(template))
                    .ifPresent(template -> html.print(template.getHtml()));
        }
    }

    private Optional<SsrBlockImpl> getBlock(String id, Supplier<SsrBlockImpl> supplier) {
        SsrBlockImpl block = template.getOrAdd(id, supplier).orElse(null);
        if (template != null)
            block.id(id);
        return Optional.ofNullable(block);
    }

    @Override
    public SsrTemplate template() {
        return template;
    }

    @Override
    public void addField(String... field) {
        throw new RuntimeException("不再使用");
    }

    @Override
    public Optional<SsrBlockImpl> getBlock(String templateId) {
        return template.get(templateId);
    }

    public SsrBlockStyleDefault createDefaultStyle() {
        return new SsrBlockStyleDefault();
    }

    public UISsrChunk setTemplateId(String id) {
        template.id(id);
        return this;
    }

}