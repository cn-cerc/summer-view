package cn.cerc.ui.ssr.chart;

import java.util.Objects;

import javax.persistence.Column;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.core.ViewDisplay;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.base.ISupportPanel;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.other.VuiDataCardRuntime;
import cn.cerc.ui.ssr.page.IVuiEnvironment;
import cn.cerc.ui.ssr.page.VuiEnvironment;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.VuiDataService;

public abstract class VuiAbstractChart extends VuiControl
        implements ICommonSupportChart, ISupportPanel, ISupplierBlock {

    protected ImageConfigImpl imageConfig;
    protected StringBuilder builder;
    protected SsrBlock block = new SsrBlock();

    @Column
    protected String title = "";

    @Column(name = "宽度占比")
    protected int width = 2;

    @Column(name = "高度占比")
    protected int height = 2;

    @Column(name = "用户自定义显示")
    protected ViewDisplay display_option = ViewDisplay.选择显示;

    @Column
    protected Binder<VuiDataService> binder = new Binder<>(this, VuiDataService.class);

    @Column(name = "皮肤")
    protected SkinTypeEnum skin = SkinTypeEnum.通用;

    @Column(name = "刷新频率(毫秒)")
    protected int refreshTime = 10000;

    @Column(name = "是否对接BDAI")
    protected boolean jointBDAI = false;

    @Override
    public SsrBlock request(ISsrBoard owner) {
        this.builder = new StringBuilder();
        builder.append(
                "<div role='chart' class='${_class}' data-height='${_height}' data-code='${_cardCode}' data-skin='${_skin}'>");
        block.option("_class", "flex" + width);
        block.option("_height", String.valueOf(height));
        block.option("_skin", String.valueOf(skin.ordinal()));
        String cardCode = "";
        if (canvas().environment() instanceof VuiEnvironment environment)
            cardCode = environment.getPageCode().replace(".execute", "");
        block.option("_cardCode", cardCode);
        block.option("_refreshTime", String.valueOf(refreshTime));
        block.option("_templateId", "");

        builder.append(String.format("""
                <div class='chartTitle'>
                    <span>${_title}</span>${if _jointBDAI}<div class='aiBox' onclick='playDataCardAIAnalysis(this);'>
                        <img src='%s' />
                        <img src='%s' />
                    </div>${endif}
                </div>
                """, getImage("images/icon/ai.png"), getImage("images/icon/ai_kanban.png")));
        block.option("_jointBDAI", jointBDAI ? "1" : "");

        buildContent();

        builder.append("</div>");
        block.option("_title", title);

        block.text(this.builder.toString());
        block.display(display_option.ordinal());
        return block();
    }

    public void buildContent() {
        builder.append("<div class='content'></div>");
    };

    @Override
    public void saveEditor(RequestReader reader) {
        super.saveEditor(reader);
        if (Utils.isEmpty(title))
            this.title = reader.getString("binder")
                    .map(serviceId -> canvas().getMember(serviceId, binder.targetType()).orElse(null))
                    .map(VuiDataService::serviceDesc)
                    .orElse(reader.getString("title").orElse(""));
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        SsrBlock block = Objects.requireNonNull(block());
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.binder.init();
            this.binder.target().ifPresent(item -> item.callByInit(false));// 让DataService不要在初始化时调用服务
            this.request(null);
            var board = this.findOwner(ISsrBoard.class);
            if (board != null) {
                board.addBlock(title, block);
            }
            break;
        case SsrMessage.InitContent:
            IVuiEnvironment env = canvas().environment();
            if (env instanceof VuiDataCardRuntime runtime) {
                block.option("_templateId", runtime.templateId());
            } else if (env instanceof VuiEnvironment environment) {
                String templateId = environment.getPageCode() + "_panel";
                block.id(templateId);
                block.option("_templateId", templateId);
            }
            this.binder.target().ifPresent(item -> {
                block.option("_service", item.service());
                block.option("_dataIn", item.dataIn().json());
                String title = item.serviceDesc();
                block.option("_data_title", title + this.getClass().getSimpleName());
                block.option("_title", title);
            });
            break;
        }
    }

    protected SsrBlock block() {
        return block;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public VuiAbstractChart title(String title) {
        this.title = title;
        return this;
    }

    public enum SkinTypeEnum {
        通用,
        无边框;
    }

    @Override
    public void output(HtmlWriter html) {
        html.print(block.html());
    }

    public String getImage(String url) {
        if (imageConfig == null)
            imageConfig = Application.getBean(ImageConfigImpl.class);
        return imageConfig.getCommonFile(url);
    }
}
