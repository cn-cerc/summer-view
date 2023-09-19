package cn.cerc.ui.ssr.report;

import java.io.IOException;
import java.net.URL;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfDiv;
import com.itextpdf.text.pdf.PdfDiv.PositionType;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.ssr.core.VuiCommonComponent;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VuiCommonComponent
public class RptPanelImage extends AbstractRptPanelControl {
    private static final Logger log = LoggerFactory.getLogger(RptPanelImage.class);

    @Column
    String imageUrl = "";

    @Override
    public void output(HtmlWriter html) {
        try {
            Image image = Image.getInstance(new URL(imageUrl));
            PdfDiv content = new PdfDiv();
            content.addElement(image);
            content.setPosition(PositionType.RELATIVE);
            float top = properties.top().map(Double::floatValue).map(RptUtils::pxTomm).orElse(0f) - div.getContentHeight();
            content.setTop(top);
            properties.left().map(Double::floatValue).map(RptUtils::pxTomm).ifPresent(content::setLeft);
            properties.width().map(Double::floatValue).map(RptUtils::pxTomm).ifPresent(width -> {
                image.scaleAbsoluteWidth(width);
                content.setWidth(width);
                div.setContentWidth(div.getContentWidth() + width);
            });
            properties.height().map(Double::floatValue).map(RptUtils::pxTomm).ifPresent(height -> {
                image.scaleAbsoluteHeight(height);
                content.setHeight(height);
                div.setContentHeight(div.getContentHeight() + height);
            });
            div.addElement(content);
        } catch (BadElementException | IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
