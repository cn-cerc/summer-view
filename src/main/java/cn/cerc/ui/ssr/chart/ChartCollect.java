package cn.cerc.ui.ssr.chart;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.VuiEnvironment;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Description("单列滚动")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class ChartCollect extends VuiAbstractChart {
    private static final Logger log = LoggerFactory.getLogger(ChartCollect.class);

    private SsrBlock block = new SsrBlock("");

    @Override
    public void saveEditor(RequestReader reader) {
        super.saveEditor(reader);
        if (Utils.isEmpty(title))
            this.title = reader.getString("binder")
                    .map(serviceId -> canvas().getMember(serviceId, binder.targetType()).orElse(null))
                    .map(VuiDataService::serviceDesc)
                    .orElse(reader.getString("title").orElse(""));
    }

    public ChartCollect() {
        init();
    }

    private void init() {
        block.option("_data", "");
        imageConfig = Application.getBean(ImageConfigImpl.class);
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public ChartCollect title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.binder.init();
            break;
        case SsrMessage.FailOnService:
            String title1 = this.binder.target().get().serviceDesc();
            block.option("_data_title", title1 + this.getClass().getSimpleName());
            block.option("_title", title1);
            if (sender == this.binder.target().get()) {
                String msg = (String) msgData;
                block.option("_msg", Utils.isEmpty(msg) ? "统计服务异常" : msg);
            }
            break;
        case SsrMessage.RefreshProperties:
        case SsrMessage.InitProperties:
            Optional<VuiDataService> serviceOpt = this.binder.target();
            if (serviceOpt.isPresent()) {
                if (sender == serviceOpt.get()) {
                    DataSet dataSet = serviceOpt.get().dataSet();
                    String title = dataSet.head().getString("title");
                    block.option("_data_title", title + this.getClass().getSimpleName());
                    block.option("_title", title);
                    if (!dataSet.eof()) {
                        block.dataSet(dataSet);
                        block.option("_data", "1");
                        block.onCallback("value", () -> {
                            return String.format("<li>%s</li>", dataSet.getString(dataSet.fields().get(0).code()));
                        });
                    } else
                        block.option("_msg", Utils.isEmpty(dataSet.message()) ? "暂无统计数据" : dataSet.message());
                }
            } else
                log.warn("{} 绑定的数据源 {} 找不到", this.getId(), this.binder.targetId());
            break;
        case SsrMessage.InitContent:
            if (canvas().environment() instanceof VuiEnvironment environment) {
                String templateId = environment.getPageCode() + "_panel";
                block.id(templateId);
                block.option("_templateId", templateId);
            }
            break;
        }

    }

    @Override
    public SsrBlock request(ISsrBoard owner) {
        owner.addBlock(this.title, block.text(String.format("""
                <div role='chart' data-title='${_data_title}'>
                <div class='opera' title='隐藏此图表' onclick='hideChart("${_templateId}", "%s")'><img src='%s' /></div>
                <div class='content'>
                <div class='chartTitle'>${_title}</div>
                ${if not _data}<div role='noData'>
                    <img src='%s' />
                    <span>${_msg}</span>
                </div>${else}
                <div class='scroll'>
                    <ul class='tabBody'>
                    ${dataset.begin}
                        ${callback(value)}
                    ${dataset.end}
                    </ul>
                </div>
                <script>$(function(){initChartScroll('${_data_title}')})</script>
                ${endif}
                </div>
                </div>
                """, title, imageConfig.getCommonFile("images/icon/hide.png"),
                imageConfig.getCommonFile("images/Frmshopping/notDataImg.png"))));
        block.id(title).display(display_option.ordinal());
        return block;
    }

}
