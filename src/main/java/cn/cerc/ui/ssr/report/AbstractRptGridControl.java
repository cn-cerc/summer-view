package cn.cerc.ui.ssr.report;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;

import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class AbstractRptGridControl extends VuiControl implements ISupportRptGrid {
    protected PdfPCell header;
    protected PdfPCell content;
    protected int total;
    protected DataSet dataSet;

    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    int width;
    @Column
    RptCellAlign align = RptCellAlign.Center;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.initPdfGridHeader:
            if (msgData instanceof PdfPCell header)
                this.header = header;
            break;
        case SsrMessage.initPdfGridContent:
            if (msgData instanceof PdfPCell content)
                this.content = content;
            break;
        case SsrMessage.InitDataIn:
            if (sender == getOwner() && msgData instanceof DataSet dataSet) {
                this.dataSet = dataSet;
            }
        }
    }

    @Override
    public void output(HtmlWriter html) {
        if (total++ == 0) {
            header.setPhrase(new Paragraph(title, RptFontLibrary.f10()));
            return;
        }
        if (dataSet != null) {
            content.setPhrase(new Paragraph(content(), RptFontLibrary.f10()));
            content.setHorizontalAlignment(align.cellAlign());
        }
    }

    protected abstract String content();

    @Override
    public void title(String title) {
        this.title = title;
    }

    @Override
    public void field(String field) {
        this.field = field;
    }

    @Override
    public void width(int width) {
        this.width = width;
    }

    @Override
    public int width() {
        return this.width;
    }

    @Override
    public RptCellAlign align() {
        return align;
    }

}
