package cn.cerc.ui.ssr.page;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("返回页面")
public class SsrReturnPage extends SsrComponent implements ISupportVisualContainer {
    SsrBlock block = new SsrBlock();
    @Column
    String href = "";
    @Column
    String title = "";

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitHeader:
            if (Utils.isEmpty(this.href) || Utils.isEmpty(this.title))
                return;
            if (msgData instanceof IHeader impl) {
                impl.addLeftMenu(this.href, this.title);
            }
            break;
        }
    }

    @Override
    public String getIdPrefix() {
        return "returnPage";
    }

}