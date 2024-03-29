package cn.cerc.ui.style;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;

public class SsrDefine implements Iterable<SsrTemplateImpl>, SsrOptionImpl {
    private static final Logger log = LoggerFactory.getLogger(SsrDefine.class);
    private Map<String, SsrTemplateImpl> items = new LinkedHashMap<>();
    public static final String BeginFlag = "begin";
    public static final String EndFlag = "end";
    private String templateText;
    private String id;
    private boolean strict = true;
    private HashMap<String, String> options;
    private DataRow dataRow;
    private DataSet dataSet;

    public SsrDefine() {
    }

    public SsrDefine(String templateText) {
        this.templateText = templateText;
        this.createItems(this, templateText);
    }

    public SsrDefine(Class<?> class1, String id) {
        this.id = class1.getSimpleName() + "_" + id;
        this.templateText = SsrUtils.getTempateFileText(class1, id);
        this.createItems(this, templateText);
    }

    private void createItems(SsrDefine ssrDefine, String templateText) {
        int start, end;
        var line = templateText.trim();
        if (Utils.isEmpty(line))
            return;
        String define = BeginFlag;
        while (line.length() > 0) {
            if ((start = line.indexOf("${define")) > -1 && (end = line.indexOf("}", start)) > -1) {
                var buff = line.substring(0, start).trim();
                if (buff.length() > 0)
                    addItem(define, buff);
                // 取大括号中间
                define = line.substring(start + 9, end).trim();
                // 大括号之后
                line = line.substring(end + 1, line.length()).trim();
            } else {
                break;
            }
            addItem(define, line);
        }
    }

    private void addItem(String blockText, String templateText) {
        var template = new SsrTemplate(templateText).setDefine(this);
        String key = null;
        int site;
        if ((site = blockText.indexOf(" ")) > 0) {
            key = blockText.substring(0, site);
            for (var param : blockText.substring(site + 1, blockText.length()).split(" "))
                addParam(key, template, param);
        } else
            key = blockText;
        template.setId(key);
        this.addItem(key, template);
    }

    private void addParam(String blockId, SsrTemplate template, String param) {
        var site = param.indexOf("=");
        if (site > -1) {
            var left = param.substring(0, site).trim();
            var right = param.substring(site + 1, param.length()).trim();
            if ((right.startsWith("'") && right.endsWith("'")) || ((right.startsWith("\"") && right.endsWith("\""))))
                right = right.substring(1, right.length() - 1);
            // 存入到选项参数
            template.setOption(left, right);
        } else {
            log.warn("参数必须以等于符号进行定义与赋值");
        }
    }

    public Map<String, SsrTemplateImpl> items() {
        return this.items;
    }

    public Optional<SsrTemplateImpl> get(String templateId) {
        return Optional.ofNullable(this.items.get(templateId));
    }

    public Optional<SsrTemplateImpl> getOrAdd(String templateId, Supplier<SsrTemplateImpl> supplier) {
        Objects.requireNonNull(supplier);
        SsrTemplateImpl template = this.get(templateId).orElse(null);
        if (template == null) {
            template = supplier.get();
            if (template != null)
                this.items().put(templateId, template);
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

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Iterator<SsrTemplateImpl> iterator() {
        return this.items.values().iterator();
    }

    public void addItem(String id, SsrTemplate template) {
        template.setDefine(this);
        this.items.put(id, template);
    }

    @Override
    public boolean isStrict() {
        return strict;
    }

    @Override
    public SsrDefine setStrict(boolean strict) {
        this.strict = strict;
        return this;
    }

    @Override
    public SsrOptionImpl setOption(String key, String value) {
        if (options == null)
            options = new HashMap<>();
        if (value == null)
            options.remove(key);
        else
            options.put(key, value);
        return this;
    }

    @Override
    public Optional<String> getOption(String key) {
        if (options == null)
            return Optional.empty();
        return Optional.ofNullable(options.get(key));
    }

    protected SsrDefine setDataRow(DataRow dataRow) {
        this.dataRow = dataRow;
        return this;
    }

    protected DataRow getDataRow() {
        return dataRow;
    }

    protected SsrDefine setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    protected DataSet getDataSet() {
        return dataSet;
    }

}
