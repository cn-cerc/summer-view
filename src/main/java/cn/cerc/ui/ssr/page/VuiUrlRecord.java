package cn.cerc.ui.ssr.page;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UrlRecord;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class VuiUrlRecord extends VuiControl implements ISupportUrl {

    @Column
    String title = "";
    @Column
    String url = "";
    @Column
    String target = "";

    @Override
    public UrlRecord urlRecord() {
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
