package cn.cerc.ui.ssr.core;

import java.util.ArrayList;
import java.util.Iterator;

public class SsrNodes implements Iterable<ISsrNode> {
    private ArrayList<ISsrNode> items;
    private String templateText;

    public SsrNodes(String templateText) {
        this.templateText = templateText;
        this.items = SsrUtils.createNodes(templateText);
        CompressNodes.run(items);
    }

    public void addNode(ISsrNode node) {
        this.items.add(node);
    }

    public ISsrNode get(int index) {
        return items.get(index);
    }

    public int size() {
        return items.size();
    }

    @Override
    public Iterator<ISsrNode> iterator() {
        return items.iterator();
    }

    public String templateText() {
        return templateText;
    }

}
