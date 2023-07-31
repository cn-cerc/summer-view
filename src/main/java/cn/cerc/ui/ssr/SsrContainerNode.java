package cn.cerc.ui.ssr;

import java.util.ArrayList;
import java.util.List;

public abstract class SsrContainerNode extends SsrValueNode {
    private List<SsrNodeImpl> items = new ArrayList<SsrNodeImpl>();

    public SsrContainerNode(String text) {
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