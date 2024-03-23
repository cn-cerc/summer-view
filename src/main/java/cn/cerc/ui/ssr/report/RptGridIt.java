package cn.cerc.ui.ssr.report;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.VuiCommonComponent;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class RptGridIt extends AbstractRptGridControl {
    @Column
    String title = "序";
    @Column
    String field = "";

    @Override
    protected String content() {
        return String.valueOf(total++);
    }

}
