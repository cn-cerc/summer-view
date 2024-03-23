package cn.cerc.ui.ssr.chart;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.chart.ChartBar.ChartBarDirection;
import cn.cerc.ui.ssr.core.VuiCommonComponent;

@Component
@Description("折线图")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class ChartLine extends VuiAbstractChart {
    @Column(name = "是否以柱状图方式展示")
    boolean isBar = false;
    @Column(name = "排列方向")
    ChartBarDirection direction = ChartBarDirection.水平;

    @Override
    public void buildContent() {
        builder.append("<div class='content' data-direction='${_direction}'></div>");
        block.option("_direction", String.valueOf(direction.ordinal()));
        builder.append(
                "<script>$(function(){buildChartByService(`${_service}`, '${_type}', '${_cardCode}', ${_dataIn}, ${_refreshTime})})</script>");
        block.option("_type", isBar ? "bar" : "line");
        block.option("_dataIn", "");
    }

}
