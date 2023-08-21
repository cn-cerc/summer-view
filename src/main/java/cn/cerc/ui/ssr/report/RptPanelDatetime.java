package cn.cerc.ui.ssr.report;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.form.DatetimeKindEnum;
import cn.cerc.ui.ssr.form.ISupplierDataRow;
import cn.cerc.ui.ssr.source.Binder;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RptPanelDatetime extends VuiControl implements ISupportRptPanel {
    @Column
    String field;
    @Column
    DatetimeKindEnum kind = DatetimeKindEnum.Datetime; 
    @Column
    Binder<ISupplierDataRow> dataRow = new Binder<>(this, ISupplierDataRow.class);
}
