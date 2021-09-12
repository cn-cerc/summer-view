package cn.cerc.ui.fields;

import cn.cerc.ui.core.UIComponent;

public class UploadField extends AbstractField {

    public UploadField(UIComponent owner, String name, String field) {
        super(owner, name, field, 5);
        this.setHtmType("file");
    }

}
