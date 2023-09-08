package cn.cerc.ui.ssr.service;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.DataValidateException;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiSearchDataIn extends VuiContainer<ISupportFilter> implements ISupportServiceHandler {

    private DataSet dataIn;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitDataIn:
            if (msgData instanceof DataSet dataIn)
                this.dataIn = dataIn;
            break;
        }
    }

    public List<ISupportFilter> getFilterFields() throws DataValidateException {
        DataRow head = dataIn.head();
        List<ISupportFilter> searchFields = getCommponetsByClass(ISupportFilter.class);
        for (ISupportFilter field : searchFields) {
            if (field.required() && !head.hasValue(field.field())) {
                throw new DataValidateException(String.format("%s 不能为空", field.title()));
            }
        }
        return searchFields;
    }

}
