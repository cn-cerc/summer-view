package cn.cerc.ui.style;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.Utils;

public class SsrUtils {
    private static final Logger log = LoggerFactory.getLogger(SsrUtils.class);

    public static ArrayList<SsrNodeImpl> createNodes(String templateText) {
        var nodes = new ArrayList<SsrNodeImpl>();
        int start, end;
        var line = templateText.trim();
        while (line.length() > 0) {
            if ((start = line.indexOf("${")) > -1 && (end = line.indexOf("}", start)) > -1) {
                if (start > 0)
                    nodes.add(new SsrTextNode(line.substring(0, start)));
                var text = line.substring(start + 2, end);
                if (SsrCallbackNode.is(text))
                    nodes.add(new SsrCallbackNode(text));
                else if (SsrListItemNode.is(text))
                    nodes.add(new SsrListItemNode(text));
                else if (SsrMapKeyNode.is(text))
                    nodes.add(new SsrMapKeyNode(text));
                else if (SsrMapValueNode.is(text))
                    nodes.add(new SsrMapValueNode(text));
                else if (SsrDataSetRecNode.is(text))
                    nodes.add(new SsrDataSetRecNode(text));
                else if (SsrDataSetItemNode.is(text))
                    nodes.add(new SsrDataSetItemNode(text));
                else
                    nodes.add(new SsrValueNode(text));
                line = line.substring(end + 1, line.length());
            } else {
                nodes.add(new SsrTextNode(line));
                break;
            }
        }
        return nodes;
    }

    public static String getTempateFileText(Class<?> class1, String id) {
        var fileName = class1.getSimpleName() + ".html";
        if (!Utils.isEmpty(id))
            fileName = class1.getSimpleName() + "_" + id + ".html";

        var file = class1.getResourceAsStream(fileName);
        var list = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8));
        String line;
        var sb = new StringBuffer();
        boolean start = false;
        try {
            while ((line = list.readLine()) != null) {
                var text = line.trim();
                if ("<body>".equals(text))
                    start = true;
                else if ("</body>".equals(text))
                    break;
                else if (start)
                    sb.append(text);
            }
            return sb.toString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

}
