package cn.cerc.ui.ssr.chart;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Description("饼图")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class ChartPie extends VuiAbstractChart {
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

    public ChartPie() {
        super();
        init();
    }

    private void init() {
        block.option("_data_title", "");
        block.option("_title", "");
        block.option("_msg", "");
        block.option("_data", "");
        block.option("_show_eye", "1");
        block.option("_templateId", "");
        imageConfig = Application.getBean(ImageConfigImpl.class);
    }

    @Override
    public SsrBlock request(ISsrBoard owner) {
        block.text(String.format(
                """
                        <div role='chart' data-title='${_data_title}' class='flex${_width}'>
                            <div class='chartTitle'>${_title}</div>
                            ${if _show_eye}
                                <div class='opera' title='隐藏此图表' onclick='hideChart("${_templateId}", "%s")'><img src='%s' /></div>
                            ${endif}
                            <div class='content'></div>
                            <script>$(function(){buildPieChartByService(`${_service}`, '${_data_title}', ${_dataIn})})</script>
                        </div>
                        """,
                title, imageConfig.getCommonFile("images/icon/hide.png")));
        block.id(title).display(display_option.ordinal());
        block.option("_width", String.valueOf(width));
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

}
