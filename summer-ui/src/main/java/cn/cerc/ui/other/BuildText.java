package cn.cerc.ui.other;

import cn.cerc.db.core.DataRow;
import cn.cerc.mis.core.HtmlWriter;

public interface BuildText {
    void outputText(DataRow record, HtmlWriter html);
}
