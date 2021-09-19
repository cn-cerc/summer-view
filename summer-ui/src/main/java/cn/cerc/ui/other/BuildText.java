package cn.cerc.ui.other;

import cn.cerc.core.DataRow;
import cn.cerc.ui.core.HtmlWriter;

public interface BuildText {
    void outputText(DataRow record, HtmlWriter html);
}
