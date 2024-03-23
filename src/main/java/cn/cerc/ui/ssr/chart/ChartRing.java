package cn.cerc.ui.ssr.chart;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.VuiCommonComponent;

@Component
@Description("数据仪表盘")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class ChartRing extends VuiAbstractChart {
    @Column(name = "表盘类型")
    ChartRingType ringType = ChartRingType.环形表盘;

    @Override
    public void buildContent() {
        builder.append(
                """
                        <div class='content' data-ringtype='${_ringType}'></div>
                        <script>$(function(){buildRingChartByService(`${_service}`, '${_cardCode}', ${_dataIn}, ${_refreshTime})})</script>""");
        block.option("_ringType", String.valueOf(ringType.ordinal()));
    }

    public enum ChartRingType {
        环形表盘,
        仪表盘
    }
}
