package cn.cerc.ui.ssr.chart;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.chart.ChartBar.ChartBarDirection;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.page.VuiEnvironment;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Description("折线图")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class ChartLine extends VuiAbstractChart {
    private SsrBlock block = new SsrBlock("");

    @Column(name = "是否以柱状图方式展示")
    boolean isBar = false;
    @Column(name = "排列方向")
    ChartBarDirection direction = ChartBarDirection.水平;
    @Column(name = "折线图类型")
    ChartLineType lineType = ChartLineType.折线图;

    public ChartLine() {
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
    public SsrBlock request(ISsrBoard owner) {
        block.text(String
                .format("""
                        <div role='chart' data-title='${_data_title}' class='flex${_width}' data-height="${_height}" data-code='${_cardCode}' data-direction='${_direction}'
                        data-skin='${_skin}' data-linetype='${_lineType}'>
                            <div class='chartTitle'>${_title}</div>
                            <div class='content'></div>
                            <script>$(function(){buildChartByService(`${_service}`, '${_type}', '${_data_title}', ${_dataIn})})</script>
                        </div>
                        """));
        block.option("_type", isBar ? "bar" : "line");
        String cardCode = "";
        if (canvas().environment() instanceof VuiEnvironment environment)
            cardCode = environment.getPageCode().replace(".execute", "");
        block.option("_cardCode", cardCode);
        block.option("_width", String.valueOf(width));
        block.option("_height", String.valueOf(height));
        block.option("_direction", String.valueOf(direction.ordinal()));
        block.option("_skin", String.valueOf(skin.ordinal()));
        block.option("_lineType", String.valueOf(lineType.ordinal()));
        block.id(title).display(display_option.ordinal());
        return block;
    }

    @Override
    public void output(HtmlWriter html) {
        html.print(block.html());
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
    protected SsrBlock block() {
        return block;
    }

    public enum ChartLineType {
        折线图,
        曲线图;
    }

}
