package cn.cerc.ui.ssr.form;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.page.ISupportCanvas;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("通用表单")
@VuiCommonComponent
public class VuiCommonForm extends VuiContainer<ISupportCommonForm> implements ISupportCanvas {

    @Column(name = "动作(action)")
    String action = "";

    @Override
    public void output(HtmlWriter html) {
        html.print("<form id='%s' action='%s'>", getId(), action);
        super.output(html);
        html.print("</form>");
    }

    @Override
    public String getIdPrefix() {
        return "form";
    }

}
