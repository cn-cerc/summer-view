package cn.cerc.ui.grid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataRowSourceImpl;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UIDataField extends UIComponent {
    private static final Logger log = LoggerFactory.getLogger(UIDataField.class);
    private DataRowSourceImpl source;
    private String fieldCode;

    public UIDataField(UIComponent owner) {
        super(owner);
        this.source = findOwner(DataRowSourceImpl.class);
        if (source == null) {
            log.error("在 owner 中找不到 UIDataViewImpl");
            throw new RuntimeException("在 owner 中找不到 UIDataViewImpl");
        }
    }

    public UIDataField setField(String fieldCode) {
        this.fieldCode = fieldCode;
        return this;
    }

    public String fieldCode() {
        return fieldCode;
    }

    @Override
    public void output(HtmlWriter html) {
        html.print(source.current().getText(fieldCode));
    }

}
