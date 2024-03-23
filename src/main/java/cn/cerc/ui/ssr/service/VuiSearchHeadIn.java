package cn.cerc.ui.ssr.service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class VuiSearchHeadIn extends VuiAbstractServiceHeadIn<ISupportFilter> {

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        super.onMessage(sender, msgType, msgData, targetId);
        switch (msgType) {
        case SsrMessage.InitDataIn:
            if (sender == canvas() && msgData instanceof DataSet dataIn)
                sendMessageToChild(msgType, dataIn.head());
            break;
        case SsrMessage.initSqlWhere:
            if (getOwner() == sender)
                sendMessageToChild(msgType, msgData);
            break;
        }
    }

}
