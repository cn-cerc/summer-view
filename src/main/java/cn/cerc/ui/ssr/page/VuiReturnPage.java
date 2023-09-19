package cn.cerc.ui.ssr.page;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierDataRow;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("返回页面")
@VuiCommonComponent
public class VuiReturnPage extends VuiComponent implements ISupportCanvas {
    SsrBlock block = new SsrBlock();
    @Column
    String href = "";
    @Column
    String title = "";
    @Column
    Binder<ISupplierDataRow> binder = new Binder<>(this, ISupplierDataRow.class);
    private IHeader header;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            binder.init();
            break;
        case SsrMessage.InitHeader:
            if (Utils.isEmpty(this.href) || Utils.isEmpty(this.title))
                return;
            if (msgData instanceof IHeader impl)
                this.header = impl;
            break;
        case SsrMessage.InitContent:
            String url = this.href;
            if (binder.target().isPresent()) {
                SsrBlock block = new SsrBlock(this.href);
                block.dataRow(binder.target().get().dataRow());
                url = block.html();
            }
            header.addLeftMenu(url, this.title);
            break;
        }
    }

    @Override
    public String getIdPrefix() {
        return "returnPage";
    }

}