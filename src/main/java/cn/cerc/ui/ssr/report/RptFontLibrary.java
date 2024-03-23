package cn.cerc.ui.ssr.report;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class RptFontLibrary {

    private static BaseFont bfChinese;
    // 设置中文字体和字体样式
    private static Font f8;
    private static Font f10;
    private static Font f18;

    static {
        try {
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            f8 = new Font(bfChinese, 8, Font.NORMAL);
            f10 = new Font(bfChinese, 10, Font.NORMAL);
            f18 = new Font(bfChinese, 18, Font.NORMAL);
        } catch (DocumentException | IOException e) {
        }
    }

    public static Font f8() {
        return f8;
    }

    public static Font f10() {
        return f10;
    }

    public static Font f18() {
        return f18;
    }

    public static Font getFont(int fontSize) {
        return switch (fontSize) {
        case 8 -> f8;
        case 10 -> f10;
        case 18 -> f18;
        default -> new Font(bfChinese, fontSize, Font.NORMAL);
        };
    }

}
