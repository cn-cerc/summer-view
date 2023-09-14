package cn.cerc.ui.ssr.page;

import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("重定向页面")
public class VuiRedirectPage extends VuiComponent implements ISupportCanvas {
    SsrBlock block = new SsrBlock();
    @Column
    String href = "";
    @Column
    String title = "";
    @Column
    Binder<VuiDataService> service = new Binder<>(this, VuiDataService.class);
    @Column
    RedirectEnum redirect = RedirectEnum.None;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.service.init();
            break;
        case SsrMessage.SuccessOnService:
            if (this.redirect == RedirectEnum.SuccessOnService) {
                if (Utils.isEmpty(this.href))
                    return;
                Optional<VuiDataService> optSvr = this.service.target();
                SsrBlock url = new SsrBlock(this.href);
                if (optSvr.isPresent()) {
                    VuiDataService svr = optSvr.get();
                    DataSet dataSet = svr.dataSet();
                    if (!dataSet.eof())
                        url.dataSet(dataSet);
                    url.dataRow(dataSet.head());
                }
                canvas().redirectPage(url.html());
            }
            break;
        }
    }

}