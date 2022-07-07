package cn.cerc.ui.columns;

import cn.cerc.db.core.DataSet;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IForm;
import cn.cerc.ui.core.UIComponent;

public class ItColumn extends AbstractColumn implements IDataColumn {

    private boolean readonly;
    private boolean hidden;

    public ItColumn(UIComponent owner) {
        super(owner);
        this.setName("序");
    }

    @Override
    public void outputCell(HtmlWriter html) {
        DataSet dataSet = this.getRecord().dataSet();
        if (dataSet == null) {
            html.print("dataSet is null");
            return;
        }
        html.print(String.valueOf(dataSet.recNo()));

        if (this.getOrigin() instanceof IForm) {
            IForm form = (IForm) this.getOrigin();
            if (form.getClient().isPhone()) {
                html.print("#");
            }
        }
    }

    @Override
    public void outputLine(HtmlWriter html) {

    }

    @Override
    public boolean isReadonly() {
        return readonly;
    }

    @Override
    public ItColumn setReadonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public ItColumn setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

}
