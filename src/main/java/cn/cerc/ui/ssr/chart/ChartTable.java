package cn.cerc.ui.ssr.chart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.FieldMeta;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.VuiEnvironment;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Description("数据表格")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChartTable extends VuiAbstractChart {
    private static final Logger log = LoggerFactory.getLogger(ChartTable.class);

    private SsrBlock block = new SsrBlock("");

    public ChartTable() {
        super();
        init();
    }

    private void init() {
        block.option("_data", "");
        imageConfig = Application.getBean(ImageConfigImpl.class);
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
    public String title() {
        return title;
    }

    @Override
    public ChartTable title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.binder.init();
            break;
        case SsrMessage.RefreshProperties:
        case SsrMessage.InitProperties:
        case SsrMessage.AfterSubmit:
            if (this.binder.target().isEmpty()) {
                log.warn("未设置数据源：dataSet");
                break;
            }
            var bean = this.binder.target();
            if (bean.isPresent()) {
                if (sender == bean.get()) {
                    VuiDataService service = bean.get();
                    DataSet dataSet = service.dataSet();
                    String title = dataSet.head().getString("title");
                    block.option("_data_title", title + this.getClass().getSimpleName());
                    block.option("_title", title);
                    block.toList(dataSet.fields().getItems().stream().map(FieldMeta::name).toList());
                    block.dataSet(dataSet);
                    if (!dataSet.eof())
                        block.option("_data", "1");
                    block.onCallback("spanContent", () -> {
                        StringBuilder builder = new StringBuilder();
                        dataSet.records().forEach((row) -> {
                            builder.append("<li>");
                            dataSet.fields().forEach((meta) -> {
                                builder.append(String.format("<span>%s</span>", row.getString(meta.code())));
                            });
                            builder.append("</li>");
                        });
                        return builder.toString();
                    });
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
        owner.addBlock(title, block.text(String.format("""
                <div role='chart' data-title='${_data_title}'>
                <div class='opera' title='隐藏此图表' onclick='hideChart("${_templateId}", "%s")'><img src='%s' /></div>
                <div class='content'>
                <div class='chartTitle'>${_title}</div>
                ${if not _data}<div role='noData'>
                    <img src='%s' />
                    <span>数据源为空或者未绑定数据源</span>
                </div>${else}
                <div class='tabHead'>
                    ${list.begin}
                        <span>${list.value}</span>
                    ${list.end}
                </div>
                <div class='scroll'>
                    <ul class='tabBody'>
                    ${callback(spanContent)}
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
