package cn.cerc.ui.ssr.report;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierDataSet;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RptGrid extends VuiContainer<ISupportRptGrid> implements ISupportRpt {
    @Column
    Binder<ISupplierDataSet> dataSet = new Binder<>(this, ISupplierDataSet.class);

}
