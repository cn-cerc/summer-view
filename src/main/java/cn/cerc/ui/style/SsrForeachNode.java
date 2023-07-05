package cn.cerc.ui.style;

import java.util.ArrayList;
import java.util.List;

public abstract class SsrForeachNode extends SsrValueNode {
    private List<SsrNodeImpl> items = new ArrayList<SsrNodeImpl>();

    public SsrForeachNode(String text) {
        super(text);
    }

    public void addItem(SsrNodeImpl child) {
        items.add(child);
    }

    public List<SsrNodeImpl> getItems() {
        return items;
    }

    @Override
    public String getText() {
        var sb = new StringBuffer();
        sb.append("${").append(this.getField()).append("}");
        for (var item : this.getItems())
            sb.append(item.getText());
        sb.append("${").append(getEndFlag()).append("}");
        return sb.toString();
    }

    protected abstract String getEndFlag();
}
