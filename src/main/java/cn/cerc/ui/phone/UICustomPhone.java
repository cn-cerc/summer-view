package cn.cerc.ui.phone;

import cn.cerc.mis.core.Application;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.ImageConfigImpl;

public abstract class UICustomPhone extends UIComponent {
    private ImageConfigImpl imageConfig;

    public UICustomPhone(UIComponent owner) {
        super(owner);
    }

    protected String getImage(String imgSrc) {
        if (imageConfig == null)
            imageConfig = Application.getBean(ImageConfigImpl.class);
        return imageConfig == null ? imgSrc : imageConfig.getCommonFile(imgSrc);
    }

    protected String getProductImage(String imgSrc) {
        if (imageConfig == null)
            imageConfig = Application.getBean(ImageConfigImpl.class);
        return imageConfig == null ? imgSrc : imageConfig.getProductFile(imgSrc);
    }

}
