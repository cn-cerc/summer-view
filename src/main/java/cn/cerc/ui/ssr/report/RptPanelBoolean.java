package cn.cerc.ui.ssr.report;

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

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RptPanelBoolean extends AbstractRptPanelControl {
    private DataRow dataRow;

    @Column
    String field = "";
    @Column
    String trueText = "是";
    @Column
    String falseText = "否";
    @Column
    Binder<ISupplierDataRow> binder = new Binder<>(this, ISupplierDataRow.class);

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
        String text = dataRow.getBoolean(field) ? trueText : falseText;
        div.addElement(buildContent(text));
    }

}
