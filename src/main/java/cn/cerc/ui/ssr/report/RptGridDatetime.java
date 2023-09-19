package cn.cerc.ui.ssr.report;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.form.DatetimeKindEnum;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class RptGridDatetime extends AbstractRptGridControl {
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    DatetimeKindEnum kind = DatetimeKindEnum.Datetime;

    @Override
    protected String content() {
        if (dataSet != null) {
            String val = switch (kind) {
            case Datetime -> dataSet.getDatetime(field).toString();
            case OnlyDate -> dataSet.getFastDate(field).toString();
            case OnlyTime -> dataSet.getDatetime(field).format("HH:mm:ss");
            case YearMonth -> dataSet.getDatetime(field).format("yyyy-MM");
            };
            return val;
        }
        return "";
    }

}
