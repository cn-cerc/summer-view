package cn.cerc.ui.core;

import java.util.List;

import cn.cerc.mis.core.IOriginOwner;

public interface IComponent extends IOriginOwner {

    int getComponentCount();

    UIComponent addComponent(UIComponent child);

    UIComponent removeComponent(IComponent component);

    List<UIComponent> getComponents();

    UIComponent getComponent(int index);
}
