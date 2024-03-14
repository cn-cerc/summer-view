package cn.cerc.ui.ssr.chart;

import java.util.ArrayList;
import java.util.List;

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
@Description("数据表格")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class ChartTable extends VuiAbstractChart {
    private static final Logger log = LoggerFactory.getLogger(ChartTable.class);

    private SsrBlock block = new SsrBlock("");

    public ChartTable() {
        super();
        init();
    }

    private void init() {
        block.option("_data", "");
        block.option("_data_title", "");
        block.option("_title", "");
        block.option("_msg", "");
        block.option("_templateId", "");
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
            block.option("_service", this.binder.target().get().service());
            if (sender == this.binder.target().get()) {
                String msg = (String) msgData;
                block.option("_msg", Utils.isEmpty(msg) ? "统计服务异常" : msg);
            }
            break;
        case SsrMessage.RefreshProperties:
        case SsrMessage.InitProperties:
        case SsrMessage.AfterSubmit:

            var bean = this.binder.target();
            if (bean.isPresent()) {
                if (sender == bean.get()) {
                    VuiDataService service = bean.get();
                    DataSet dataSet = service.dataSet();
                    String title = this.binder.target().get().serviceDesc();
                    block.option("_data_title", title + this.getClass().getSimpleName());
                    block.option("_title", title);
                    block.option("_service", this.binder.target().get().service());
                    if (!dataSet.eof()) {
                        block.dataSet(dataSet);
                        block.option("_data", "1");
                        Boolean hasWidth = dataSet.head().fields().exists("width_");
                        List<Integer> list = new ArrayList<Integer>();
                        if (hasWidth) {
                            String widthStr = dataSet.head().getString("width_");
                            String[] widthArr = widthStr.split(",");
                            for (String str : widthArr) {
                                int width = Integer.parseInt(str);
                                list.add(width);
                            }
                        }

                        Boolean isCustomWidth = hasWidth && list.size() == dataSet.fields().size();

                        block.onCallback("spanContent", () -> {
                            StringBuilder builder = new StringBuilder();
                            dataSet.records().forEach((row) -> {
                                builder.append("<li>");
                                int index = 0;
                                for (FieldMeta meta : dataSet.fields()) {
                                    if (!("width_".equals(meta.code()))) {
                                        builder.append("<span");
                                        if (isCustomWidth)
                                            builder.append(String.format(" style='flex: %s;'", list.get(index)));
                                        builder.append(String.format(">%s</span>", row.getString(meta.code())));
                                    }
                                    index++;
                                }
                                builder.append("</li>");
                            });
                            return builder.toString();
                        });
                        block.onCallback("headContent", () -> {
                            StringBuilder builder = new StringBuilder();
                            int index = 0;
                            for (FieldMeta meta : dataSet.fields()) {
                                builder.append("<span");
                                if (isCustomWidth)
                                    builder.append(String.format(" style='flex: %s;'", list.get(index)));
                                builder.append(String.format(">%s</span>", meta.name()));
                                index++;
                            }
                            return builder.toString();
                        });
                    } else
                        block.option("_msg", Utils.isEmpty(dataSet.message()) ? "暂无统计数据" : dataSet.message());
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
    public SsrBlock request(ISsrBoard owner) {
        block.text(String.format(
                """
                        <div role='chart' data-title='${_data_title}' class='flex${_width}' data-height="${_height}" data-code='${_cardCode}'
                        data-skin='${_skin}'>
                            <div class='chartTitle'>${_title}</div>
                            <div class='content'>
                                ${if not _data}
                                    <div role='noData'>
                                        <img src='%s' />
                                        <span>${_msg}</span>
                                    </div>
                                ${else}
                                    <div class='tabHead'>
                                        ${callback(headContent)}
                                    </div>
                                    <div class='scroll'>
                                        <ul class='tabBody'>
                                        ${callback(spanContent)}
                                        </ul>
                                    </div>
                                    <script>$(function(){initChartScroll('${_data_title}')})</script>
                                ${endif}
                                <script>$(function(){ refreshChartTableByService('${_data_title}', '${_service}', '${_cardCode}') })</script>
                            </div>
                        </div>
                        """,
                imageConfig.getCommonFile("images/Frmshopping/notDataImg.png")));
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
    public void output(HtmlWriter html) {
        html.print(block.html());
    }

    @Override
    protected SsrBlock block() {
        return block;
    }

}
