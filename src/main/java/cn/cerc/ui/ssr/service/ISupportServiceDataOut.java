package cn.cerc.ui.ssr.service;

import cn.cerc.db.core.DataSet;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;

public interface ISupportServiceDataOut extends ISupportServiceData {

    DataSet dataOut();

    public static DataSet findDataOut(UIComponent component, DataSet dataSet) {
        for (UIComponent child : component.getComponents()) {
            if (child instanceof VuiComponent vuiComponent && child instanceof ISupportServiceDataOut serviceOut) {
                vuiComponent.onMessage(component, SsrMessage.InitDataIn, dataSet, null);
                return serviceOut.dataOut();
            }
            if (child instanceof ISupportServiceHandler)
                return findDataOut(child, dataSet);
        }
        return new DataSet();
    }

}
