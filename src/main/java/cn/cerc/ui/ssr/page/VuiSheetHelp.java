package cn.cerc.ui.ssr.page;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.EditorGrid;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierDataRow;
import cn.cerc.ui.ssr.source.VuiDataService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class VuiSheetHelp extends VuiContainer<ISupportHelp> implements ISupportToolbar {
    private IToolbar toolbar;

    @Column
    String caption = "";
    @Column
    Binder<ISupplierDataRow> binder = new Binder<>(this, ISupplierDataRow.class);

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
        case SsrMessage.InitBinder:
            binder.init();
            break;
        case SsrMessage.InitToolbar:
            if (msgData instanceof IToolbar toolbar) {
                this.toolbar = toolbar;
            }
            break;
        case SsrMessage.RefreshProperties:
            boolean continueNext = false;
            // 将DataService的数据转发给子组件
            if (binder.target().isPresent()) {
                ISupplierDataRow row = binder.target().get();
                if (row instanceof VuiDataService && sender == row) {
                    sendDataToChild(row.dataRow());
                    continueNext = true; // 如果是DataService发送的刷新数据，那么就继续执行下面的初始化内容
                }
            }
            if (!continueNext)
                break;
        case SsrMessage.InitContent:
            if (binder.target().isPresent()) { // 如果不存在绑定对象则正常输出
                ISupplierDataRow row = binder.target().get();
                // 如果绑定对象是VuiDataService，那么就需要等待Service发送刷新数据
                if (row instanceof VuiDataService && sender != row) {
                    break;
                } else {
                    sendDataToChild(row.dataRow());
                }
            }
            List<String> lines = this.getComponents().stream().map(t -> {
                if (t instanceof ISupportHelp help)
                    return help;
                return null;
            }).filter(Objects::nonNull).map(ISupportHelp::line).filter(line -> !Utils.isEmpty(line)).toList();
            if (lines.size() > 0) {
                IVuiSheetHelp sheet = toolbar.addSheet(IVuiSheetHelp.class);
                if (!Utils.isEmpty(caption))
                    sheet.setCaption(caption);
                lines.forEach(sheet::addLine);
            }
            break;
        }
    }

    private void sendDataToChild(DataRow dataRow) {
        for (UIComponent component : this) {
            if (component instanceof VuiComponent vui) {
                vui.onMessage(this, SsrMessage.InitDataIn, dataRow, null);
            }
        }
    }

    @Override
    public String getIdPrefix() {
        return "sheetHelp";
    }

}
