package cn.cerc.ui.ssr.report;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.VuiCommonComponent;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class RptPanelText extends AbstractRptPanelControl {
    @Column
    String text = "";

    @Override
    public void output(HtmlWriter html) {
        div.addElement(buildContent(text));
    }

}
