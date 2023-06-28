package cn.cerc.ui.style;

import java.util.ArrayList;
import java.util.List;

public abstract class UIForeachNode extends UIValueNode {
    private List<UISsrNodeImpl> items = new ArrayList<UISsrNodeImpl>();

    public UIForeachNode(String text) {
        super(text);
    }

    public void addItem(UISsrNodeImpl child) {
        items.add(child);
    }

    public List<UISsrNodeImpl> getItems() {
        return items;
    }

}
