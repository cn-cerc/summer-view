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
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.other.VuiDataCardRuntime;
import cn.cerc.ui.ssr.page.IVuiEnvironment;
import cn.cerc.ui.ssr.page.VuiEnvironment;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Description("单列滚动")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class ChartCollect extends VuiAbstractChart {
    private static final Logger log = LoggerFactory.getLogger(ChartCollect.class);

    @Override
    public void buildContent() {
        builder.append(String.format("""
                <div class='content'>
                    ${if _noData}
                        <div role='noData'>
                            <img src='%s' />
                            <span>${_msg}</span>
                        </div>
                    ${else}
                        <div class='scroll'>
                            <ul class='tabBody'>
                            ${dataset.begin}
                                ${callback(value)}
                            ${dataset.end}
                            </ul>
                        </div>
                        <script>$(function(){initChartScroll('${_dataCard}')})</script>
                    ${endif}
                </div>""", getImage("images/Frmshopping/notDataImg.png")));
        block.option("_noData", "");
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
        case SsrMessage.RefreshProperties:
        case SsrMessage.InitProperties:
            Optional<VuiDataService> serviceOpt = this.binder.target();
            if (serviceOpt.isPresent()) {
                if (sender == serviceOpt.get()) {
                    DataSet dataSet = serviceOpt.get().dataSet();
                    String title = this.binder.target().get().serviceDesc();
                    block.option("_data_title", title + this.getClass().getSimpleName());
                    block.option("_title", title);
                    if (!dataSet.eof()) {
                        block.dataSet(dataSet);
                        block.option("_noData", "");
                        block.onCallback("value", () -> {
                            return String.format("<li>%s</li>", dataSet.getString(dataSet.fields().get(0).code()));
                        });
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
        case SsrMessage.FailOnService:
            String title1 = this.binder.target().get().serviceDesc();
            block.option("_data_title", title1 + this.getClass().getSimpleName());
            block.option("_title", title1);
            if (sender == this.binder.target().get()) {
                String msg = (String) msgData;
                block.option("_msg", Utils.isEmpty(msg) ? "统计服务异常" : msg);
            }
            break;
        }
    }

}
