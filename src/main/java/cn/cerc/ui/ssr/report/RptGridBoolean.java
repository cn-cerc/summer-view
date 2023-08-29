package cn.cerc.ui.ssr.report;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.VuiControl;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RptGridBoolean extends VuiControl implements ISupportRptGrid {
    @Column
    String title;
    @Column
    String field;
}