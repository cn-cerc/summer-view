package cn.cerc.ui.ssr.source;

import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;

public abstract class VuiCustomMapValue extends VuiComponent implements ISupplierMap, IBinders {

    protected Binders binders = new Binders();

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitProperties:
            binders.sendMessage(this, SsrMessage.InitMapSourceDone, null, null);
            break;
        }
    }

    @Override
    public Binders binders() {
        return this.binders;
    }

}
