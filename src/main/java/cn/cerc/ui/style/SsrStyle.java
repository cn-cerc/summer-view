package cn.cerc.ui.style;

import java.util.ArrayList;
import java.util.Iterator;

public class SsrStyle implements Iterable<SsrNodeImpl> {
    ArrayList<SsrNodeImpl> nodes;
    private String templateText;
    private SsrTemplate template;

    public SsrStyle(String templateText) {
        this.templateText = templateText;
        this.nodes = SsrUtils.createNodes(templateText);
        CompressNodes.run(nodes);
    }

    public void addNode(SsrNodeImpl node) {
        this.nodes.add(node);
    }

    public SsrNodeImpl get(int index) {
        return nodes.get(index);
    }

    public int size() {
        return nodes.size();
    }

    @Override
    public Iterator<SsrNodeImpl> iterator() {
        return nodes.iterator();
    }

    public String templateText() {
        return templateText;
    }

    public void setTemplate(SsrTemplate template) {
        this.template = template;
    }

    public SsrTemplate template() {
        return template;
    }

}
