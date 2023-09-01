package cn.cerc.ui.ssr.chart;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.base.ISupportPanel;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Description("折线图")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChartLine extends VuiControl implements ICommonSupportChart, ISupportPanel {
    private ImageConfigImpl imageConfig;
    private static final Logger log = LoggerFactory.getLogger(ChartBar.class);

    @Column
    String title = "";
    @Column
    String field = "";
    @Column(name = "是否以柱状图方式展示")
    boolean isBar = false;
    @Column
    Binder<VuiDataService> binder = new Binder<>(this, VuiDataService.class);
    DataSet dataSet;

    public ChartLine() {
        super();
        init();
    }

    private void init() {
        if (imageConfig == null)
            imageConfig = Application.getBean(ImageConfigImpl.class);
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

        html.println("<script type='text/javascript' src='%s'></script>",
                imageConfig.getCommonFile("js/echarts/echarts.js"));
        String title = dataSet.head().getString("title") + this.getClass().getSimpleName();
        html.println("<div role='chart' data-title='%s'>", title);
        html.println("</div>");
        html.println("<script>$(function(){buildChartByDataSet(`%s`, '%s', '%s')})</script>", dataSet.json(),
                isBar ? "bar" : "line", title);
    }

    @Override
    public String fields() {
        return this.fields();
    }

    @Override
    public ICommonSupportChart field(String field) {
        return this.field(field);
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

}
