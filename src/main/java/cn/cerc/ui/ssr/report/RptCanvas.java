package cn.cerc.ui.ssr.report;

import java.nio.charset.StandardCharsets;

import javax.persistence.Column;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import cn.cerc.db.core.ClassResource;
import cn.cerc.mis.SummerMIS;
import cn.cerc.mis.print.ExportPdf;
import cn.cerc.ui.ssr.core.PropertiesReader;
import cn.cerc.ui.ssr.core.RptOutPutDeviceEnum;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.page.IVuiEnvironment;
import cn.cerc.ui.ssr.page.VuiCanvas;

public class RptCanvas extends VuiCanvas {
    private static final Logger log = LoggerFactory.getLogger(RptCanvas.class);
    private static final ClassResource res = new ClassResource(ExportPdf.class, SummerMIS.ID);

    private IVuiEnvironment environment;
    private Document document;

    @Column(name = "页面宽(mm)")
    int width = 210; // 默认为 A4 纸的宽度，单位为 mm
    @Column(name = "页面高(mm)")
    int height = 297; // 默认为 A4 纸的高度，单位为 mm
    @Column(name = "左页边距")
    float marginLeft = 36;
    @Column(name = "右页边距")
    float marginRight = 36;
    @Column(name = "上页边距")
    float marginTop = 36;
    @Column(name = "下页边距")
    float marginBottom = 36;
    @Column(name = "是否横向打印")
    boolean broadwise = false;
    /**
     * 定义输出设备，默认为屏幕
     */
    @Column(name = "输出设备")
    RptOutPutDeviceEnum outputDevice = RptOutPutDeviceEnum.screen;
    @Column
    String author = res.getString(1, "地藤系统");
    @Column
    String subject = res.getString(2, "地藤系统报表文件");

    public RptCanvas(IVuiEnvironment environment) {
        super(null);// 此处将环境传入进去后会导致自己的属性被重新初始化
        this.environment = environment;
        if (environment != null) {
            this.setOwner(environment.getContent());
            var json = environment.loadProperties();
            this.readProperties(new PropertiesReader(json));
            this.setSupportClass(environment.getSupportClass());
        }
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        super.onMessage(sender, msgType, msgData, targetId);
        switch (msgType) {
        case SsrMessage.InitResponse:
            if (msgData instanceof HttpServletResponse response) {
                if (outputDevice == RptOutPutDeviceEnum.file) {
                    response.setCharacterEncoding("UTF-8");// 设置相应内容的编码格式
                    String fname = new String(this.title().getBytes(), StandardCharsets.ISO_8859_1);
                    response.setHeader("Content-Disposition", "attachment;filename=" + fname + ".pdf");
                }
                response.setContentType("application/pdf");// 定义输出类型
            }
            break;
        case SsrMessage.InitPdfDocument:
            if (msgData instanceof Document doc) {
                doc.addAuthor(author);
                doc.addSubject(subject);
                doc.addCreationDate();

                doc.addTitle(title());
                // 页标题
                Paragraph title = new Paragraph(title(), RptFontLibrary.f18());
                title.setAlignment(Element.ALIGN_CENTER);
                try {
                    doc.add(title);
                    // 空一行
                    doc.add(new Paragraph(" ", RptFontLibrary.f18()));
                } catch (DocumentException e) {
                    log.error(e.getMessage(), e);
                }
            }
            break;
        case SsrMessage.InitPdfWriter:
            if (msgData instanceof PdfWriter writer && outputDevice == RptOutPutDeviceEnum.printer)
                writer.addJavaScript("this.print({bUI: true, bSilent: true,bShrinkToFit:true});", false);
            break;
        }
    }

    public Document document() {
        if (document == null) {
            double x = width * 72 / 25.4;
            double y = height * 72 / 25.4;
            Rectangle rectangle = new Rectangle((int) x, (int) y);
            if (broadwise) {
                rectangle = rectangle.rotate();
            }
            document = new Document(rectangle, marginLeft, marginRight, marginTop, marginBottom);
        }
        return document;
    }

    @Override
    public IVuiEnvironment environment() {
        return environment;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public float marginLeft() {
        return marginLeft;
    }

    public float marginRight() {
        return marginRight;
    }

    public float marginTop() {
        return marginTop;
    }

    public float marginBottom() {
        return marginBottom;
    }

    public float pageWidth() {
        return document.getPageSize().getWidth() - marginLeft - marginRight;
    }

    public float pageHeight() {
        return document.getPageSize().getHeight() - marginTop - marginBottom;
    }

}
