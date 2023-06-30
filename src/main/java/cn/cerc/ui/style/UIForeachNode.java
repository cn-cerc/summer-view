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

    @Override
    public String getSourceText() {
        var sb = new StringBuffer();
        sb.append("${").append(this.getField()).append("}");
        for (var item : this.getItems())
            sb.append(item.getSourceText());
        sb.append("${").append(getEndFlag()).append("}");
        return sb.toString();
    }

    protected abstract String getEndFlag();
}
