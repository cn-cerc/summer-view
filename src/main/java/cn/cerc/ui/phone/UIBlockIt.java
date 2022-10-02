package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSetSourceImpl;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIBlockIt extends UIComponent {

    public UIBlockIt() {
        this(null);
    }

    public UIBlockIt(UIComponent owner) {
        super(owner);
        this.setRootLabel("span");
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        var impl = findOwner(DataSetSourceImpl.class);
        if (impl != null)
            html.print(impl.dataSet().recNo() + "#");
        this.endOutput(html);
    }

}
