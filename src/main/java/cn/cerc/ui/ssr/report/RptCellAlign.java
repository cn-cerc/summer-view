package cn.cerc.ui.ssr.report;

import com.itextpdf.text.pdf.PdfPCell;

public enum RptCellAlign {
    Center(PdfPCell.ALIGN_CENTER),
    Left(PdfPCell.ALIGN_LEFT),
    Right(PdfPCell.ALIGN_RIGHT);

    private int cellAlign;

    RptCellAlign(int cellAlign) {
        this.cellAlign = cellAlign;
    }

    public int cellAlign() {
        return cellAlign;
    }

}
