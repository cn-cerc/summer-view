package cn.cerc.ui.ssr.report;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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
