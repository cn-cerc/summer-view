package cn.cerc.ui.ssr.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompressNodes {
    private static final Logger log = LoggerFactory.getLogger(CompressNodes.class);
    private int size = 0;

    public static void run(List<ISsrNode> nodes) {
        if (log.isDebugEnabled()) {
            log.info("压缩前 total: {}", nodes.size());
            for (var node : nodes)
                listNode(node, 0);
        }
        var obj = new CompressNodes();
        obj.execute(nodes);
        if (log.isDebugEnabled()) {
            log.info("总压缩执行次数: {}", obj.size());
            log.info("压缩后 total: {}", nodes.size());
            for (var node : nodes)
                listNode(node, 0);
        }
    }

    private void execute(List<ISsrNode> nodes) {
        if (nodes.size() < 2)
            return;
        while (compress(nodes, SsrIfNode.Sign))
            continue;
        while (compress(nodes, SsrListNode.Sign))
            continue;
        while (compress(nodes, SsrMapNode.Sign))
            continue;
        while (compress(nodes, SsrDatasetNode.Sign))
            continue;
        for (var node : nodes) {
            if (node instanceof SsrContainerNode item)
                this.execute(item.getItems());
        }
    }

    private boolean compress(List<ISsrNode> nodes, SsrContainerSignRecord sign) {
        this.size++;
        // 查找开头与结尾
        var start = -1;
        var stop = -1;
        // 是否成对指示器
        var count = 0;
        var first = -1;
        SsrContainerNode container = null;
        for (var i = 0; i < nodes.size(); i++) {
            var node = nodes.get(i);
            if (node instanceof SsrValueNode item) {
                if (item instanceof SsrContainerNode)
                    continue;
                var field = item.getField();
                if (field.startsWith(sign.beginFlag())) {
                    if (container == null) {
                        container = sign.supper().createObject(field);
                        start = i;
                        first = count;
                    }
                    count++;// 匹配次数
                }
                if (field.equals(sign.endFlag())) {
                    if (--count == first) { // 与第1次匹配相符
                        stop = i;
                        break;
                    }
                }
            }
        }

        // 没有找到可归集对象
        if (start == -1 || stop == -1)
            return false;

        // 先复制到 temp 变量中
        var temp = new ArrayList<ISsrNode>();
        temp.addAll(nodes);
        nodes.clear();

        // 开始归集
        for (var i = 0; i < temp.size(); i++) {
            if (i == start)
                nodes.add(container);
            else if (i == stop) {
                continue;
            } else if (i > start && i < stop)
                container.addItem(temp.get(i));
            else
                nodes.add(temp.get(i));
        }
        return true;
    }

    public int size() {
        return size;
    }

    private static void listNode(ISsrNode root, int level) {
        System.out.print("" + level);
        if (root instanceof SsrContainerNode item) {
            System.out.println("-- " + item.getText() + " size: " + item.getItems().size());
            for (var child : item.getItems())
                listNode(child, level + 1);
        } else {
            System.out.print("-- " + root.getClass().getSimpleName());
            System.out.print(":" + String.join("", Collections.nCopies(level, " ")));
            System.out.println(root.getText());
        }
    }

}
