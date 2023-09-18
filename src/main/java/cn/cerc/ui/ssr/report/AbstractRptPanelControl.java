package cn.cerc.ui.ssr.report;

import javax.persistence.Column;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfDiv;
import com.itextpdf.text.pdf.PdfDiv.PositionType;

import cn.cerc.ui.ssr.core.ViewPropertiesRecord;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.SsrMessage;

public abstract class AbstractRptPanelControl extends VuiControl implements ISupportRptPanel {
    protected ViewPropertiesRecord properties = new ViewPropertiesRecord(properties());
    protected PdfDiv div;

    @Column
    int fontSize = 10;

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.initPdfPanel:
            if (msgData instanceof PdfDiv div)
                this.div = div;
            break;
        }
    }

    protected PdfDiv buildContent(String content) {
        PdfDiv contentDiv = new PdfDiv();
        contentDiv.setPosition(PositionType.RELATIVE);
        contentDiv.addElement(new Phrase(content, RptFontLibrary.getFont(fontSize)));
        float top = properties.top().map(Double::floatValue).map(RptUtils::pxTomm).orElse(0f) - div.getContentHeight();
        contentDiv.setTop(top);
        properties.left().map(Double::floatValue).map(RptUtils::pxTomm).ifPresent(contentDiv::setLeft);
        properties.width().map(Double::floatValue).map(RptUtils::pxTomm).ifPresent(width -> {
            contentDiv.setWidth(width);
            div.setContentWidth(div.getContentWidth() + width);
        });
        properties.height().map(Double::floatValue).map(RptUtils::pxTomm).ifPresent(height -> {
            contentDiv.setHeight(height);
            div.setContentHeight(div.getContentHeight() + height);
        });
        return contentDiv;
    }

}
