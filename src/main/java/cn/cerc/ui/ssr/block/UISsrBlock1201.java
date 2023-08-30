package cn.cerc.ui.ssr.block;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.core.UIComponent;

@Deprecated
public class UISsrBlock1201 extends VuiBlock1201 {

    public UISsrBlock1201() {
        this(null);
    }

    public UISsrBlock1201(UIComponent owner) {
        super(owner);
    }

}
