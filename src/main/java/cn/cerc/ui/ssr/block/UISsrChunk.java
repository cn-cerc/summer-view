package cn.cerc.ui.ssr.block;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrTemplate;
import cn.cerc.ui.ssr.editor.ISsrBoard;

public class UISsrChunk extends UIComponent implements ISsrBoard {
    private static final Logger log = LoggerFactory.getLogger(UISsrChunk.class);
    private SsrTemplate template;
    private SsrBlockStyleDefault defaultStle;
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

    private Optional<SsrBlock> getBlock(String id, Supplier<SsrBlock> supplier) {
        SsrBlock block = template.getOrAdd(id, supplier).orElse(null);
        if (template != null)
            block.id(id);
        return Optional.ofNullable(block);
    }

    @Override
    public SsrTemplate template() {
        return template;
    }

    @Override
    public Optional<SsrBlock> getBlock(String templateId) {
        return template.get(templateId);
    }

    /**
     * 请改使用 defaultStyle()
     * 
     * @return
     */
    @Deprecated
    public SsrBlockStyleDefault createDefaultStyle() {
        return defaultStyle();
    }

    public SsrBlockStyleDefault defaultStyle() {
        if (defaultStle == null)
            defaultStle = new SsrBlockStyleDefault();
        return defaultStle;
    }

    public UISsrChunk setTemplateId(String id) {
        template.id(id);
        return this;
    }

    @Override
    public List<String> columns() {
        throw new RuntimeException("不再使用");
    }

}
