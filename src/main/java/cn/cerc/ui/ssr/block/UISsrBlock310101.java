package cn.cerc.ui.ssr.block;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.core.UIComponent;

@Deprecated
public class UISsrBlock310101 extends VuiBlock310101 {

    public UISsrBlock310101() {
        this(null);
    }

    public UISsrBlock310101(UIComponent owner) {
        super(owner);
    }

}
