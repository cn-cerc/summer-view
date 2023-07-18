package cn.cerc.ui.style;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.Utils;

public class SsrUtils {
    private static final Logger log = LoggerFactory.getLogger(SsrUtils.class);

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
