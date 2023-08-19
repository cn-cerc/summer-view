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
import cn.cerc.ui.ssr.core.SsrComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.SsrDataService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("重定向页面")
public class SsrRedirectPage extends SsrComponent implements ISupportVisualContainer {
    SsrBlock block = new SsrBlock();
    @Column
    String href = "";
    @Column
    String title = "";
    @Column
    Binder<SsrDataService> service = new Binder<>(SsrDataService.class);
    @Column
    RedirectEnum redirect = RedirectEnum.None;

    public SsrRedirectPage() {
        super();
        this.service.owner(this);
    }

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
                Optional<SsrDataService> optSvr = this.service.target();
                SsrBlock url = new SsrBlock(this.href);
                if (optSvr.isPresent()) {
                    SsrDataService svr = optSvr.get();
                    DataSet dataSet = svr.getDataSet();
                    if (!dataSet.eof())
                        url.setDataSet(dataSet);
                }
                getContainer().redirectPage(url.getHtml());
            }
            break;
        }
    }

}