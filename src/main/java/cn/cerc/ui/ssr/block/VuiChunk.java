package cn.cerc.ui.ssr.block;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrTemplate;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.ISupportCanvas;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("手机表格块")
@VuiCommonComponent
public class VuiChunk extends VuiContainer<ISupportBoard> implements ISsrBoard, ISupportCanvas {
    private static final Logger log = LoggerFactory.getLogger(VuiChunk.class);
    private SsrTemplate template;
    private SsrBlockStyleDefault defaultStle;
    public static final String ListBegin = "list.begin";
    public static final String ListEnd = "list.end";
    private ImageConfigImpl imageConfig;

    @Column
    Binder<VuiDataService> dataSet = new Binder<>(this, VuiDataService.class);

    public VuiChunk() {
        super(null);
        template = new SsrTemplate();
    }

    public VuiChunk(UIComponent owner) {
        super(owner);
        template = new SsrTemplate();
    }

    public VuiChunk(UIComponent owner, String templateText) {
        super(owner);
        template = new SsrTemplate(templateText);
    }

    public VuiChunk(UIComponent owner, Class<?> class1, String id) {
        super(owner);
        template = new SsrTemplate(class1, id);
    }

    public DataSet dataSet() {
        return template.dataSet();
    }

    public VuiChunk dataSet(DataSet dataSet) {
        template.dataSet(dataSet);
        return this;
    }
    
    protected String getImage(String imgSrc) {
        if (imageConfig == null)
            imageConfig = Application.getBean(ImageConfigImpl.class);
        return imageConfig == null ? imgSrc : imageConfig.getCommonFile(imgSrc);
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.dataSet() == null) {
            log.error("dataSet is null");
            return;
        }
        if (dataSet().size() > 0) {
            getBlock(SsrTemplate.BeginFlag, () -> new SsrBlock("<div role='chunkBox'>").template(template))
                    .ifPresent(template -> html.print(template.html()));
            var save_rec = dataSet().recNo();
            try {
                dataSet().first();
                while (dataSet().fetch()) {
                    getBlock(ListBegin, () -> new SsrBlock("<ul role='chunkBoxItem'>").template(template))
                            .ifPresent(template -> html.print(template.html()));
                    for (var component : getComponents()) {
                        if (component instanceof VuiBoard board)
                            board.template(template);
                        component.output(html);
                    }
                    getBlock(ListEnd, () -> new SsrBlock("</ul>").template(template))
                            .ifPresent(template -> html.print(template.html()));
                }
            } finally {
                dataSet().setRecNo(save_rec);
            }
            getBlock(SsrTemplate.EndFlag, () -> new SsrBlock("</div>").template(template))
                    .ifPresent(template -> html.print(template.html()));
        } else {
            html.print("""
                <div id="UINoData" class="grey">
                    <img src=%s />
                    <p>暂无数据</p>
                </div>
            """, this.getImage("images/Frmshopping/notDataImg.png"));
        }
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.dataSet.init();
            List<UIComponent> list = getComponents();
            if (!Utils.isEmpty(list)) {
                for (UIComponent component : list) {
                    this.canvas().sendMessage(this, SsrMessage.InitBinder, this.dataSet, component.getId());
                }
            }
            break;
        case SsrMessage.RefreshProperties:
        case SsrMessage.InitProperties:
        case SsrMessage.AfterSubmit:
            if (this.dataSet.target().isEmpty()) {
                log.warn("未设置数据源：dataSet");
                break;
            }
            var bean = this.canvas().getMember(this.dataSet.targetId(), VuiDataService.class);
            if (bean.isPresent())
                this.dataSet(bean.get().dataSet());
            else
                log.warn("{} 绑定的数据源 {} 找不到", this.getId(), this.dataSet.targetId());
            break;
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

    public VuiChunk setTemplateId(String id) {
        template.id(id);
        return this;
    }

    @Override
    public List<String> columns() {
        throw new RuntimeException("不再使用");
    }

}
