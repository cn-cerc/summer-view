package cn.cerc.ui.grid;

import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIDataComponent extends UIComponent {
    private final DataSet dataSet;
    private final String field;

    public UIDataComponent(UIComponent owner, DataSet dataSet, String field) {
        super(owner);
        this.dataSet = dataSet;
        this.field = field;
    }

    public final String getField() {
        return field;
    }

    public final DataSet getDataSet() {
        return dataSet;
    }

    @Override
    public void output(HtmlWriter html) {
        html.print(dataSet.current().getText(field));
    }

}
