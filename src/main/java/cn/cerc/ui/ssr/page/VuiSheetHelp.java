package cn.cerc.ui.ssr.page;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.EditorGrid;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class VuiSheetHelp extends VuiContainer<ISupportHelp> implements ISupportToolbar {

    @Override
    public void buildEditor(UIComponent content, String pageCode) {
        super.buildEditor(content, pageCode);

        var grid = new EditorGrid(content, this);
        for (UIComponent item : this)
            grid.addRow().setValue("id", item.getId()).setValue("class", item.getClass().getSimpleName());
        grid.addColumn("代码", "id", 20);
        grid.addColumn("类名", "class", 30);
        grid.build(pageCode);
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitToolbar:
            if (msgData instanceof IToolbar toolbar) {
                List<String> lines = this.getComponents().stream().map(t -> {
                    if (t instanceof ISupportHelp help)
                        return help;
                    return null;
                }).filter(Objects::nonNull).map(ISupportHelp::line).filter(line -> !Utils.isEmpty(line)).toList();
                if (lines.size() > 0) {
                    IVuiSheetHelp sheet = toolbar.addSheet(IVuiSheetHelp.class);
                    lines.forEach(sheet::addLine);
                }
            }
            break;
        }
    }

    @Override
    public String getIdPrefix() {
        return "sheetHelp";
    }

}
