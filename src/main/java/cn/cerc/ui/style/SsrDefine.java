package cn.cerc.ui.style;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SsrDefine {
    private static final Logger log = LoggerFactory.getLogger(SsrDefine.class);
    private Map<String, SsrTemplateImpl> items = new LinkedHashMap<>();
    public static final String BeginFlag = "begin";
    public static final String EndFlag = "end";
    private String templateText;

    public SsrDefine(String templateText) {
        this.templateText = templateText;
        this.createItems(this, templateText);
    }

    public SsrDefine(Class<?> class1, String id) {
        this.templateText = SsrUtils.getTempateFileText(class1, id);
        this.createItems(this, templateText);
    }

    private void createItems(SsrDefine ssrDefine, String templateText) {
        int start, end;
        var line = templateText.trim();
        StringBuffer sb = new StringBuffer();
        String define = BeginFlag;
        while (line.length() > 0) {
            if ((start = line.indexOf("${define")) > -1 && (end = line.indexOf("}", start)) > -1) {
                // 第1个字符不是大括号
                if (start > 0) {
                    sb.append(line.substring(0, start).trim());
                    addItem(define, sb.toString());
                    sb.setLength(0);
                }
                // 大括号中间
                define = line.substring(start + 9, end).trim();
                // 大括号之后
                line = line.substring(end + 1, line.length()).trim();
            } else {
                sb.append(line);
                break;
            }
        }
        if (sb.length() > 0)
            addItem(define, sb.toString());
    }

    private void addItem(String field, String text) {
        var template = new SsrTemplate(text);
        String key = null;
        int site;
        if ((site = field.indexOf(" ")) > 0) {
            key = field.substring(0, site);
            for (var param : field.substring(site + 1, field.length()).split(" "))
                addParam(template, param);
        } else
            key = field;
        items.put(key, template);
    }

    private void addParam(SsrTemplate template, String param) {
        var site = param.indexOf("=");
        if (site > -1) {
            var left = param.substring(0, site).trim();
            var right = param.substring(site + 1, param.length()).trim();
            if ((right.startsWith("'") && right.endsWith("'")) || ((right.startsWith("\"") && right.endsWith("\""))))
                template.toMap(left, right.substring(1, right.length() - 1));
            else
                template.toMap(left, right);
        } else {
            log.warn("参数必须以等于符号进行定义与赋值");
        }
    }

    public Map<String, SsrTemplateImpl> items() {
        return this.items;
    }

    public Optional<SsrTemplateImpl> get(String id) {
        return Optional.ofNullable(this.items.get(id));
    }

    public Optional<SsrTemplateImpl> getOrAdd(String id, Supplier<SsrTemplateImpl> supplier) {
        Objects.requireNonNull(supplier);
        SsrTemplateImpl template = this.get(id).orElse(null);
        if (template == null) {
            template = supplier.get();
            if (template != null)
                this.items().put(id, template);
        }
        return Optional.ofNullable(template);
    }

    public Optional<SsrTemplateImpl> getBegin() {
        return this.get(BeginFlag);
    }

    public Optional<SsrTemplateImpl> getEnd() {
        return this.get(EndFlag);
    }

    public String templateText() {
        return templateText;
    }

}
