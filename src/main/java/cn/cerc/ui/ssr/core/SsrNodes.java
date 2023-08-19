package cn.cerc.ui.ssr.core;

import java.util.ArrayList;
import java.util.Iterator;

public class SsrNodes implements Iterable<ISsrNode> {
    ArrayList<ISsrNode> items;
    private String templateText;
    private SsrBlock block;

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

    public SsrNodes block(SsrBlock block) {
        this.block = block;
        return this;
    }

    public SsrBlock block() {
        return block;
    }

    public String templateText() {
        return templateText;
    }

}
