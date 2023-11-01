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

    protected ImageConfigImpl imageConfig;

    @Column
    protected String title = "";

    @Column(name = "宽度占比")
    protected int width = 1;

    @Column(name = "用户自定义显示")
    protected ViewDisplay display_option = ViewDisplay.选择显示;

    @Column
    protected Binder<VuiDataService> binder = new Binder<>(this, VuiDataService.class);

    @Override
    public String title() {
        return title;
    }

}
