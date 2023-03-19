package cn.cerc.ui.phone;

import cn.cerc.db.core.DataSetSource;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIPhoneCellIt extends UIComponent {

    public UIPhoneCellIt() {
        this(null);
    }

    public UIPhoneCellIt(UIComponent owner) {
        super(owner);
        this.setRootLabel("span");
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        var impl = findOwner(DataSetSource.class);
        if (impl != null)
            html.print(impl.source().orElseThrow().recNo() + "#");
        this.endOutput(html);
    }

}
