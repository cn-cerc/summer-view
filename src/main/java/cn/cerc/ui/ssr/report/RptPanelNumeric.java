package cn.cerc.ui.ssr.report;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierDataRow;
import cn.cerc.ui.ssr.source.ISupplierList;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class RptPanelNumeric extends AbstractRptPanelControl {
    private DataRow dataRow;
    private List<String> list;

    @Column
    String field = "";
    @Column
    String format = "";
    @Column
    Binder<ISupplierDataRow> binder = new Binder<>(this, ISupplierDataRow.class);
    @Column
    Binder<ISupplierList> listSource = new Binder<>(this, ISupplierList.class);

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
        case SsrMessage.InitListSourceDone:
            Optional<ISupplierList> optList = listSource.target();
            if (optList.isPresent())
                this.list = optList.get().items();
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
        String text = "";
        if (listSource.target().isPresent()) {
            text = list.get(dataRow.getInt(field));
        } else {
            double value = dataRow.getDouble(field);
            text = Utils.isEmpty(format) ? String.valueOf(value) : new DecimalFormat(format).format(value);
        }
        div.addElement(buildContent(text));
    }

}
