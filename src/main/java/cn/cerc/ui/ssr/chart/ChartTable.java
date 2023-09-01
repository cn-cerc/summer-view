package cn.cerc.ui.ssr.chart;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.base.ISupportPanel;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Description("数据表格")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChartTable extends VuiControl implements ICommonSupportChart, ISupportPanel {
    private static final Logger log = LoggerFactory.getLogger(ChartTable.class);
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    Binder<VuiDataService> binder = new Binder<>(this, VuiDataService.class);
    DataSet dataSet;

    @Override
    public String fields() {
        return field;
    }

    @Override
    public ChartTable field(String field) {
        this.field = field;
        return this;
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
            if (bean.isPresent())
                dataSet = bean.get().dataSet();
            else
                log.warn("{} 绑定的数据源 {} 找不到", this.getId(), this.binder.targetId());
            break;
        }
    }

    @Override
    public void output(HtmlWriter html) {
        if (dataSet == null)
            return;
        String title = dataSet.head().getString("title") + this.getClass().getSimpleName();
        html.println("<div role='chart' data-title='%s'>", title);
        html.println("<div class='chartTitle'>%s</div>", dataSet.head().getString("title"));
        html.println("<div class='tabHead'>");
        dataSet.fields().forEach((meta) -> {
            html.println("<span>%s</span>", meta.name());
        });
        html.println("</div>");
        html.println("<div class='scroll'>");
        html.println("<ul class='tabBody'>");
        dataSet.records().forEach((row) -> {
            html.println("<li>");
            dataSet.fields().forEach((meta) -> {
                html.println("<span>%s</span>", row.getString(meta.code()));
            });
            html.println("</li>");
        });
        html.println("</ul>");
        html.println("</div>");
        html.println("</div>");
        html.println("<script>$(function(){initChartScroll('%s')})</script>", title);
    }

}
