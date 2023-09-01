package cn.cerc.ui.ssr.page;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VuiSheetUrl extends VuiContainer<ISupportUrl> implements ISupportToolbar {

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitToolbar:
            if (msgData instanceof IToolbar toolbar) {
                IVuiSheetUrl sheet = toolbar.addSheet(IVuiSheetUrl.class);
                for (UIComponent item : this) {
                    if (item instanceof ISupportUrl url) {
                        sheet.addUrl(url.urlRecord());
                    }
                }
            }
            break;
        }
    }

    @Override
    public String getIdPrefix() {
        return "sheetUrl";
    }

}
