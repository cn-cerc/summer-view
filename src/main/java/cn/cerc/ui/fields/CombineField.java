package cn.cerc.ui.fields;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.grid.lines.AbstractGridLine.IOutputOfGridLine;

public class CombineField extends AbstractField implements IFormatColumn, IOutputOfGridLine {

    public CombineField(UIComponent owner, String name) {
        super(owner, name, "");
    }

    @Override
    public void outputOfGridLine(HtmlWriter html) {

    }

}
