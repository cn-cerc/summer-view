package cn.cerc.ui.ssr.excel;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.SsrMessage;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class XlsTextCell extends VuiControl implements ISupportXlsRow {
    private static final Logger log = LoggerFactory.getLogger(XlsTextCell.class);

    @Column
    String text;
    @Column
    int column = 0;

    private WritableSheet sheet;
    private int startRow = 0;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
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
        try {
            int column = this.column > 0 ? this.column : this.getOwner().getComponents().indexOf(this);
            sheet.addCell(new Label(column, startRow, text));
        } catch (WriteException e) {
            log.error(e.getMessage(), e);
        }
    }
}
