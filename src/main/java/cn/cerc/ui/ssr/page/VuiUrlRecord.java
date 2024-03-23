package cn.cerc.ui.ssr.page;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UrlRecord;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class VuiUrlRecord extends VuiControl implements ISupportUrl {
    private DataRow dataRow;

    @Column
    String title = "";
    @Column
    String url = "";
    @Column
    String target = "";

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
    public UrlRecord urlRecord() {
        String url = this.url;
        String title = this.title;
        if (dataRow != null) {
            url = new SsrBlock(url).strict(false).dataRow(dataRow).html();
            title = new SsrBlock(title).strict(false).dataRow(dataRow).html();
        }
        UrlRecord record = new UrlRecord(url, title);
        if (!Utils.isEmpty(target))
            record.setTarget(target);
        return record;
    }

    @Override
    public String getIdPrefix() {
        return "url";
    }

}
