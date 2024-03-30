package cn.cerc.ui.ssr.core;

import java.util.ArrayList;
import java.util.List;

public abstract class SsrContainerNode extends SsrValueNode {
    private final List<ISsrNode> items = new ArrayList<>();

    public SsrContainerNode(String text) {
        super(text);
    }

    public void addItem(ISsrNode child) {
        items.add(child);
    }

    public List<ISsrNode> getItems() {
        return items;
    }

    @Override
    public String getText() {
        StringBuilder builder = new StringBuilder();
        builder.append("${").append(this.getField()).append("}");
        for (var item : this.getItems())
            builder.append(item.getText());
        builder.append("${").append(getEndFlag()).append("}");
        return builder.toString();
    }

    protected abstract String getEndFlag();

}