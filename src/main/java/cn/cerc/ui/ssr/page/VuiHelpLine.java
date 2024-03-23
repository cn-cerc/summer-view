package cn.cerc.ui.ssr.page;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class VuiHelpLine extends VuiControl implements ISupportHelp {
    private DataRow dataRow;

    @Column
    String content = "";
    @Column
    boolean isRed = false;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitDataIn:
            if (sender == getOwner() && msgData instanceof DataRow dataRow) {
                this.dataRow = dataRow;
            }
            break;
        }
    }

    @Override
    public String line() {
        if (Utils.isEmpty(content))
            return "";
        String line = content;
        if (dataRow != null)
            line = new SsrBlock(content).strict(false).dataRow(dataRow).html();
        if (isRed)
            return String.format("<span style='color:red;'>%s</span>", line);
        return line;
    }

    @Override
    public String getIdPrefix() {
        return "line";
    }
}
