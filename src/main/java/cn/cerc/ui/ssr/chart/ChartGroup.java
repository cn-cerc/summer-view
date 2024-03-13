package cn.cerc.ui.ssr.chart;

import java.util.Optional;

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
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.other.VuiDataCardRuntime;
import cn.cerc.ui.ssr.page.IVuiEnvironment;
import cn.cerc.ui.ssr.page.VuiEnvironment;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Description("数据摘要")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class ChartGroup extends VuiAbstractChart {
    private static final Logger log = LoggerFactory.getLogger(ChartGroup.class);
    private SsrBlock block = new SsrBlock("");

    @Column(name = "标题字段")
    String titleField = "key_";

    @Column(name = "取值字段")
    String valueField = "value_";

    public ChartGroup() {
        super();
        init();
    }

    public void init() {
        block.option("_title", "");
        block.option("_data_title", "");
        block.option("_msg", "");
        block.option("_noData", "");
        block.option("_templateId", "");
        imageConfig = Application.getBean(ImageConfigImpl.class);
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public ICommonSupportChart title(String title) {
        this.title = title;
        return this;
    }

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
    public SsrBlock request(ISsrBoard owner) {
        block.text(String.format("""
                <div role='chart' class='chartGroup flex${_width}' data-height="${_height}" data-code='${_cardCode}'
                data-skin='${_skin}'>
                <div class='chartTitle'>${_title}</div>
                ${if _noData}
                <div role='noData'>
                    <img src='%s' />
                    <span>${_msg}</span>
                </div>
                ${else}
                <a class='listBox' ${if _url}href='${_url}'${endif}>
                    <ul>
                    ${dataset.begin}
                        <li>
                            <span>${dataset.%s}</span>
                            <span>${dataset.%s}</span>
                        </li>
                    ${dataset.end}
                    </ul>
                </a>
                ${endif}
                <script>$(function(){ refreshChartGroupByService(`${_service}`, `${_cardCode}`) })</script>
                </div>
                """, imageConfig.getCommonFile("images/Frmshopping/notDataImg.png"), titleField, valueField));
        block.id(title).display(display_option.ordinal());
        String cardCode = "";
        if (canvas().environment() instanceof VuiEnvironment environment)
            cardCode = environment.getPageCode().replace(".execute", "");
        block.option("_cardCode", cardCode);
        block.option("_width", String.valueOf(width));
        block.option("_height", String.valueOf(height));
        block.option("_skin", String.valueOf(skin.ordinal()));
        return block;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.binder.init();
            this.request(null);
            var board = this.findOwner(ISsrBoard.class);
            if (board != null) {
                board.addBlock(title, block);
            }
            break;
        case SsrMessage.FailOnService:
            String title1 = this.binder.target().get().serviceDesc();
            block.option("_data_title", title1 + this.getClass().getSimpleName());
            block.option("_title", title1);
            if (sender == this.binder.target().get()) {
                String msg = (String) msgData;
                block.option("_noData", "1");
                block.option("_msg", Utils.isEmpty(msg) ? "统计服务异常" : msg);
            }
            break;
        case SsrMessage.RefreshProperties:
        case SsrMessage.InitProperties:

            Optional<VuiDataService> service = this.binder.target();
            if (service.isPresent()) {
                if (sender == service.get()) {
                    DataSet dataSet = service.get().dataSet();
                    String title = this.binder.target().get().serviceDesc();
                    block.option("_service", service.get().service());
                    block.option("_data_title", title + this.getClass().getSimpleName());
                    block.option("_title", title);
                    if (!dataSet.eof()) {
                        block.option("_url", dataSet.head().getString("url"));
                        block.dataSet(dataSet);
                    } else {
                        block.option("_noData", "1");
                        block.option("_msg", Utils.isEmpty(dataSet.message()) ? "暂无统计数据" : dataSet.message());
                    }
                }
            } else
                log.warn("{} 绑定的数据源 {} 找不到", this.getId(), this.binder.targetId());
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
            break;
        }
    }

    @Override
    public void output(HtmlWriter html) {
        html.print(block.html());
    }

    @Override
    protected SsrBlock block() {
        return block;
    }

}
