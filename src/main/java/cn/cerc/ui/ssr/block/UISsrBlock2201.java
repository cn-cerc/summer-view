package cn.cerc.ui.ssr.block;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.core.UIComponent;

@Deprecated
public class UISsrBlock2201 extends VuiBlock2201 {

    public UISsrBlock2201() {
        this(null);
    }

    public UISsrBlock2201(UIComponent owner) {
        super(owner);
    }

}
