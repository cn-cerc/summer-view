package cn.cerc.ui.ssr.excel;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.form.ISupplierDataRow;
import cn.cerc.ui.ssr.source.Binder;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class XlsDataCell extends VuiControl implements ISupportXlsRow {
    @Column
    String field;
    @Column
    Binder<ISupplierDataRow> dataRow = new Binder<>(ISupplierDataRow.class);
}
