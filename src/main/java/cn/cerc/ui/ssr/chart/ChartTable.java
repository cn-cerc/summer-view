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

    @Override
    public void buildContent() {
        builder.append(String.format(
                """
                        <div class='content'>
                            ${if _noData}
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
                                <script>$(function(){initChartScroll('${_cardCode}')})</script>
                            ${endif}
                            <script>$(function(){ refreshChartTableByService('${_service}', '${_cardCode}', ${_refreshTime}) })</script>
                        </div>""",
                getImage("images/Frmshopping/notDataImg.png")));
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
        case SsrMessage.AfterSubmit:
            var bean = this.binder.target();
            if (bean.isPresent()) {
                if (sender == bean.get()) {
                    VuiDataService service = bean.get();
                    DataSet dataSet = service.dataSet();
                    String title = this.binder.target().get().serviceDesc();
                    block.option("_title", title);
                    block.option("_service", this.binder.target().get().service());
                    if (!dataSet.eof()) {
                        block.dataSet(dataSet);
                        block.option("_noData", "");
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
            block.option("_title", title1);
            block.option("_service", this.binder.target().get().service());
            if (sender == this.binder.target().get()) {
                String msg = (String) msgData;
                block.option("_noData", "1");
                block.option("_msg", Utils.isEmpty(msg) ? "统计服务异常" : msg);
            }
            break;
        }
    }

}
