package cn.cerc.ui.ssr.excel;

import java.util.Optional;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierDataRow;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class XlsDataCell extends VuiControl implements ISupportXlsRow {
    private static final Logger log = LoggerFactory.getLogger(XlsDataCell.class);
    @Column
    String field;
    @Column
    Binder<ISupplierDataRow> dataRow = new Binder<>(this, ISupplierDataRow.class);
    @Column
    int column = 0;

    private WritableSheet sheet;
    private int startRow = 0;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            dataRow.init();
            break;
        case SsrMessage.InitSheet:
            if (msgData instanceof WritableSheet sheet)
                this.sheet = sheet;
            break;
        case SsrMessage.SheetNextRow:
            if (msgData instanceof Integer value)
                this.startRow += value;
            break;
        }
    }

    @Override
    public void output(HtmlWriter html) {
        if (Utils.isEmpty(field)) {
            log.warn("{} {} field is null", this.getClass().getSimpleName(), this.getId());
            return;
        }
        try {
            Optional<ISupplierDataRow> optDataRow = dataRow.target();
            if (optDataRow.isPresent()) {
                DataRow dataRow = optDataRow.get().dataRow();
                if (dataRow != null) {
                    int column = this.column > 0 ? this.column : this.getOwner().getComponents().indexOf(this);
                    sheet.addCell(new Label(column, startRow, dataRow.getString(field)));
                }
            }
        } catch (WriteException e) {
            log.error(e.getMessage(), e);
        }
    }
}
