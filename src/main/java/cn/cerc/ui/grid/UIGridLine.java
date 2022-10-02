package cn.cerc.ui.grid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.FieldMeta;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UIDataViewImpl;
import cn.cerc.ui.vcl.UITd;

public class UIGridLine extends UIComponent {
    private static final Logger log = LoggerFactory.getLogger(UIGridLine.class);

    public UIGridLine(UIComponent owner) {
        super(owner);
        this.setRootLabel("tr");
    }

    @Override
    public void output(HtmlWriter html) {
        var impl = findOwner(UIDataViewImpl.class);
        if (impl == null) {
            log.error("在 owner 中找不到 UIDataViewImpl");
            throw new RuntimeException("在 owner 中找不到 UIDataViewImpl");
        }
        var dataSet = impl.dataSet();
        dataSet.first();
        while (dataSet.fetch())
            super.output(html);
    }

    public void addField(FieldMeta meta) {
        new UIDataField(new UITd(this)).setField(meta.code());
    }

}
