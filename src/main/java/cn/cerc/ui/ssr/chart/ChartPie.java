package cn.cerc.ui.ssr.chart;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.VuiCommonComponent;

@Component
@Description("饼图")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class ChartPie extends VuiAbstractChart {
    @Column(name = "饼图类型")
    protected ChartPieType pieTyle = ChartPieType.环形饼图;

    @Override
    public void buildContent() {
        builder.append("<div class='content' data-pietype='${_pieType}'></div>");
        block.option("_pieType", String.valueOf(pieTyle.ordinal()));
        builder.append(
                "<script>$(function(){buildPieChartByService(`${_service}`, '${_cardCode}', ${_dataIn}, ${_refreshTime})})</script>");
        block.option("_dataIn", "");
    }

    public enum ChartPieType {
        普通饼图,
        环形饼图,
        玫瑰饼图
    }
}
