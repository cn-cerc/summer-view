package cn.cerc.ui.ssr.report;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RptGridBoolean extends AbstractRptGridControl {
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    String trueText = "是";
    @Column
    String falseText = "否";

    @Override
    protected String content() {
        if (dataSet != null)
            return dataSet.getBoolean(field) ? trueText : falseText;
        return "";
    }

}
