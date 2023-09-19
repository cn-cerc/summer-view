package cn.cerc.ui.ssr.excel;

import java.util.Map;
import java.util.Optional;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierMap;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class XlsStringColumn extends VuiControl implements ISupportXlsGrid {
    private static final Logger log = LoggerFactory.getLogger(XlsStringColumn.class);
    @Column
    String title;
    @Column
    String field;
    @Column
    int width;
    @Column
    Binder<ISupplierMap> mapSource = new Binder<>(this, ISupplierMap.class);

    private Map<String, String> map;

    private WritableSheet sheet;
    private int total = 0;
    private int startRow = 0;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            mapSource.init();
            break;
        case SsrMessage.InitMapSourceDone:
            Optional<ISupplierMap> optMap = mapSource.target();
            if (optMap.isPresent())
                this.map = optMap.get().items();
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
            if (this.getOwner() instanceof XlsGrid grid) {
                int column = grid.getComponents().indexOf(this);
                if (this.total++ == 0) {
                    sheet.setColumnView(column, this.width);
                    sheet.addCell(new Label(column, startRow, this.title));
                } else {
                    var dataSet = grid.dataSet();
                    if (dataSet != null) {
                        String val = dataSet.getString(field);
                        if (map != null)
                            val = map.getOrDefault(val, val);
                        var cell = new Label(column, startRow, val);
                        sheet.addCell(cell);
                    }
                }
            }
        } catch (WriteException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public ISupportXlsGrid title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public ISupportXlsGrid field(String field) {
        this.field = field;
        return this;
    }

    @Override
    public ISupportXlsGrid width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public int width() {
        return width;
    }

}
