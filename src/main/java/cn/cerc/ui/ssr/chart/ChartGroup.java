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

    @Column(name = "标题字段")
    String titleField = "key_";

    @Column(name = "取值字段")
    String valueField = "value_";

    @Override
    public void buildContent() {
        builder.append(String.format(
                """
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
                                    ${if _hasIcon}<img src="%s/${dataset.icon_}"/>${endif}
                                    <span>${dataset.%s}</span>
                                    <div id="${_cardCode}_${dataset.rec}">
                                        <span>${dataset.%s}</span>
                                    </div>
                                </li>
                            ${dataset.end}
                            </ul>
                        </a>
                        ${endif}
                        <script>$(function(){ refreshChartGroupByService(`${_service}`, `${_cardCode}`, ${_refreshTime}, `${_data}`) })</script>""",
                getImage("images/Frmshopping/notDataImg.png"), getImage("images/kanban"), titleField, valueField, valueField));
        block.option("_noData", "");
        block.option("_hasIcon", "");
        block.option("_data", "");
        block.option("_class", "chartGroup flex" + width);
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
            Optional<VuiDataService> service = this.binder.target();
            if (service.isPresent()) {
                if (sender == service.get()) {
                    DataSet dataSet = service.get().dataSet();
                    String title = this.binder.target().get().serviceDesc();
                    block.option("_service", service.get().service());
                    block.option("_data_title", title + this.getClass().getSimpleName());
                    block.option("_title", title);
                    if (!dataSet.eof()) {
                        block.option("_noData", "");
                        // 判断是否存在 icon
                        if (dataSet.fields().exists("icon_"))
                            block.option("_hasIcon", "1");
                        block.option("_url", dataSet.head().getString("url"));
                        block.option("_data", dataSet.json());
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
        }
    }

}
