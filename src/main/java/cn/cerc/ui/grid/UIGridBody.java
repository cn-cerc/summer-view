package cn.cerc.ui.grid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataSource;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIGridBody extends UIComponent {
    private static final Logger log = LoggerFactory.getLogger(UIGridBody.class);

    public UIGridBody(UIComponent owner) {
        super(owner);
        this.setRootLabel("tr");
    }

    @Override
    public void output(HtmlWriter html) {
        var impl = findOwner(DataSource.class);
        if (impl == null) {
            log.error("在 owner 中找不到 DataSource");
            throw new RuntimeException("在 owner 中找不到 DataSource");
        }
        var dataSet = impl.dataSet();
        dataSet.first();
        while (dataSet.fetch()) {
            super.output(html);
            html.println("");
        }
    }

}
