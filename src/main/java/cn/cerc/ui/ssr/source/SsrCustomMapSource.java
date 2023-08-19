package cn.cerc.ui.ssr.source;

import cn.cerc.ui.ssr.core.SsrComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;

public abstract class SsrCustomMapSource extends SsrComponent implements IMapSource, IBinders {

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
