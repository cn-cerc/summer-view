package cn.cerc.ui.ssr.chart;

import javax.persistence.Column;

import cn.cerc.ui.core.ViewDisplay;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.base.ISupportPanel;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.VuiDataService;

public abstract class VuiAbstractChart extends VuiControl
        implements ICommonSupportChart, ISupportPanel, ISupplierBlock {

    @Column
    String title = "";

    @Column(name = "用户自定义显示")
    ViewDisplay display_option = ViewDisplay.选择显示;

    ImageConfigImpl imageConfig;

    @Column
    Binder<VuiDataService> binder = new Binder<>(this, VuiDataService.class);

    @Override
    public String title() {
        return title;
    }

}
