package cn.cerc.ui.ssr.page;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.VuiControl;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiHelpLine extends VuiControl implements ISupportHelp {

    @Column
    String content = "";
    @Column
    boolean isRed = false;

    @Override
    public String line() {
        if (Utils.isEmpty(content))
            return "";
        if (isRed)
            return String.format("<span style='color:red;'>%s</span>", content);
        return content;
    }

    @Override
    public String getIdPrefix() {
        return "line";
    }
}
