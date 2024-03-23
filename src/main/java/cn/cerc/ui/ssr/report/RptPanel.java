package cn.cerc.ui.ssr.report;

import java.util.Optional;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDiv;

import cn.cerc.db.core.DataRow;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.ssr.core.ViewPropertiesRecord;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiComponent;
import cn.cerc.ui.ssr.core.VuiContainer;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierDataRow;
import cn.cerc.ui.ssr.source.ISupplierDataSet;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class RptPanel extends VuiContainer<ISupportRptPanel> implements ISupportRpt {
    private static final Logger log = LoggerFactory.getLogger(RptGrid.class);

    private ViewPropertiesRecord properties = new ViewPropertiesRecord(properties());
    private DataRow dataRow;
    private Document document;

    @Column
    String v_inner_align = "Position";
    @Column
    String v_width = "";
    @Column
    String v_height = "100";
    @Column(name = "从DataSet的头部取出")
    boolean dataSetHead = false;
    @Column
    Binder<ISupplierDataRow> binder = new Binder<>(this, ISupplierDataRow.class);

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            binder.init();
            break;
        case SsrMessage.RefreshProperties:
            Optional<ISupplierDataRow> optDataRow = binder.target();
            if (optDataRow.isPresent()) {
                ISupplierDataRow supplierDataRow = optDataRow.get();
                if (dataSetHead && supplierDataRow instanceof ISupplierDataSet dataSet) {
                    this.dataRow = dataSet.dataSet().head();
                    break;
                }
                if (sender == supplierDataRow) {
                    this.dataRow = supplierDataRow.dataRow();
                }
            }
            break;
        case SsrMessage.InitPdfDocument:
            if (msgData instanceof Document document)
                this.document = document;
            break;
        }
    }

    @Override
    public void output(HtmlWriter html) {
        PdfDiv div = new PdfDiv();
        properties.top().map(Double::floatValue).map(RptUtils::pxTomm).ifPresent(div::setTop);
        properties.left().map(Double::floatValue).map(RptUtils::pxTomm).ifPresent(div::setLeft);
        div.setWidth(this.width());
        div.setHeight(this.height());
        for (UIComponent component : getComponents()) {
            if (component instanceof VuiComponent vui) {
                vui.onMessage(this, SsrMessage.InitDataIn, dataRow, null);
                vui.onMessage(this, SsrMessage.initPdfPanel, div, null);
                component.output(html);
            }
        }
        try {
            document.add(div);
        } catch (DocumentException e) {
            log.error(e.getMessage(), e);
        }
    }

    public float width() {
        return properties.width().map(Double::floatValue).map(RptUtils::pxTomm).orElseGet(() -> {
            if (canvas() instanceof RptCanvas canvas)
                return canvas.pageWidth();
            return 0f;
        });
    }

    public float height() {
        return properties.height().map(Double::floatValue).map(RptUtils::pxTomm).orElseGet(() -> {
            if (canvas() instanceof RptCanvas canvas)
                return canvas.pageHeight();
            return 0f;
        });
    }

}
