package cn.cerc.ui.ssr.report;

import java.util.Map;
import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierDataRow;
import cn.cerc.ui.ssr.source.ISupplierMap;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RptPanelString extends AbstractRptPanelControl {
    private DataRow dataRow;
    private Map<String, String> map;

    @Column
    String field = "";
    @Column
    Binder<ISupplierDataRow> binder = new Binder<>(this, ISupplierDataRow.class);
    @Column
    Binder<ISupplierMap> mapSource = new Binder<>(this, ISupplierMap.class);

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        super.onMessage(sender, msgType, msgData, targetId);
        switch (msgType) {
        case SsrMessage.InitBinder:
            binder.init();
            break;
        case SsrMessage.InitDataIn:
            if (msgData instanceof DataRow dataRow) {
                if (binder.target().isEmpty())
                    this.dataRow = dataRow;
            }
            break;
        case SsrMessage.InitMapSourceDone:
            Optional<ISupplierMap> optMap = mapSource.target();
            if (optMap.isPresent())
                this.map = optMap.get().items();
            break;
        }
    }

    @Override
    public void output(HtmlWriter html) {
        if (Utils.isEmpty(field))
            return;
        if (binder.target().isPresent())
            this.dataRow = binder.target().get().dataRow();
        if (dataRow == null)
            return;
        String text = dataRow.getString(field);
        if (mapSource.target().isPresent())
            text = map.get(text);
        div.addElement(buildContent(text));
    }

}
