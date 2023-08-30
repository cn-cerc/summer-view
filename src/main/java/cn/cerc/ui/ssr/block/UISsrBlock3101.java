package cn.cerc.ui.ssr.block;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.core.UIComponent;

@Deprecated
public class UISsrBlock3101 extends VuiBlock3101 {

    public UISsrBlock3101() {
        this(null);
    }

    public UISsrBlock3101(UIComponent owner) {
        super(owner);
    }

}
