package cn.cerc.ui.fields;

import cn.cerc.ui.core.UIComponent;

public class UploadField extends AbstractField {

    public UploadField(UIComponent owner, String name, String field) {
        super(owner, name, field, 5);
        this.setHtmType("file");
    }

    // 用于文件上传是否可以选则多个文件
    public UploadField setMultiple(boolean multiple) {
        getContent().setSignProperty("multiple", multiple);
        return this;
    }

    @Override
    public UIComponent getContent() {
        return super.getContent();
    }

}
