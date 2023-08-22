package cn.cerc.ui.ssr.base;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.VuiControl;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiLabel extends VuiControl implements ISupportPanel {
    @Column
    String text = "";

    @Override
    public void output(HtmlWriter html) {
        html.print(text);
    }
    
}
