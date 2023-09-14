package cn.cerc.ui.ssr.service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiSearchBodyIn extends VuiAbstractServiceBodyIn<ISupportFilter> {

    private DataSet dataIn;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        super.onMessage(sender, msgType, msgData, targetId);
        switch (msgType) {
        case SsrMessage.InitDataIn:
            if (sender == canvas() && msgData instanceof DataSet dataIn)
                this.dataIn = dataIn;
            break;
        case SsrMessage.initSqlWhere:
            if (getOwner() == sender) {
                sendMessageToChild(SsrMessage.InitDataIn, dataIn.current());
                sendMessageToChild(msgType, msgData);
            }
            break;
        }
    }

}
