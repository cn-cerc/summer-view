package cn.cerc.ui.ssr;

import java.util.ArrayList;
import java.util.Iterator;

public class SsrNodes implements Iterable<SsrNodeImpl> {
    ArrayList<SsrNodeImpl> items;
    private String templateText;
    private SsrBlock block;

    public SsrNodes(String templateText) {
        this.templateText = templateText;
        this.items = SsrUtils.createNodes(templateText);
        CompressNodes.run(items);
    }

    public void addNode(SsrNodeImpl node) {
        this.items.add(node);
    }

    public SsrNodeImpl get(int index) {
        return items.get(index);
    }

    public int size() {
        return items.size();
    }

    @Override
    public Iterator<SsrNodeImpl> iterator() {
        return items.iterator();
    }

    public void block(SsrBlock block) {
        this.block = block;
    }

    public SsrBlock block() {
        return block;
    }

    public String templateText() {
        return templateText;
    }

}
