package cn.cerc.ui.ssr.core;

import java.util.Set;

public interface ISupportChildren {

    Set<Class<? extends SsrComponent>> getChildren();

    String getComponentSign();
    
    Class<?> defaultClass();
}
