package cn.cerc.ui.ssr.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSet;
import cn.cerc.db.core.Utils;

public class SsrTemplate implements Iterable<SsrBlock>, ISsrOption {
    private static final Logger log = LoggerFactory.getLogger(SsrTemplate.class);
    public static final String BeginFlag = "begin";
    public static final String EndFlag = "end";
    private LinkedHashMap<String, SsrBlock> items = new LinkedHashMap<>();
    private String templateText;
    private String id;
    private boolean strict = true;
    private HashMap<String, String> options;
    private DataRow dataRow;
    private DataSet dataSet;

    public SsrTemplate() {
    }

    public SsrTemplate(String templateText) {
        this.templateText = templateText;
        this.createItems(this, templateText);
    }

    public SsrTemplate(Class<?> class1, String id) {
        this.id = class1.getSimpleName() + "_" + id;
        this.templateText = SsrUtils.getTempateFileText(class1, id);
        this.createItems(this, templateText);
    }

    private void createItems(SsrTemplate ssrDefine, String templateText) {
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
        var template = new SsrBlock(templateText).template(this);
        String key = null;
        int site;
        if ((site = blockText.indexOf(" ")) > 0) {
            key = blockText.substring(0, site);
            for (var param : blockText.substring(site + 1, blockText.length()).split(" "))
                addParam(key, template, param);
        } else
            key = blockText;
        template.id(key);
        this.addItem(key, template);
    }

    private void addParam(String blockId, SsrBlock block, String param) {
        var site = param.indexOf("=");
        if (site > -1) {
            var left = param.substring(0, site).trim();
            var right = param.substring(site + 1, param.length()).trim();
            if ((right.startsWith("'") && right.endsWith("'")) || ((right.startsWith("\"") && right.endsWith("\""))))
                right = right.substring(1, right.length() - 1);
            // 存入到选项参数
            block.option(left, right);
        } else {
            log.warn("参数必须以等于符号进行定义与赋值");
        }
    }

    public LinkedHashMap<String, SsrBlock> items() {
        return this.items;
    }

    public Optional<SsrBlock> get(String templateId) {
        return Optional.ofNullable(this.items.get(templateId));
    }

    public Optional<SsrBlock> getOrAdd(String templateId, Supplier<SsrBlock> supplier) {
        Objects.requireNonNull(supplier);
        SsrBlock block = this.get(templateId).orElse(null);
        if (block == null) {
            block = supplier.get();
            if (block != null)
                this.items().put(templateId, block);
        }
        return Optional.ofNullable(block);
    }

    public String templateText() {
        return templateText;
    }

    public String id() {
        return id;
    }

    public void id(String id) {
        if (id != null && id.length() > 50)
            throw new RuntimeException("template id 不得超过50位长度");
        this.id = id;
    }

    @Override
    public Iterator<SsrBlock> iterator() {
        return this.items.values().iterator();
    }

    public void addItem(String id, SsrBlock block) {
        block.template(this);
        this.items.put(id, block);
    }

    @Override
    public boolean strict() {
        return strict;
    }

    @Override
    public SsrTemplate strict(boolean strict) {
        this.strict = strict;
        return this;
    }

    @Override
    public ISsrOption option(String key, String value) {
        if (options == null)
            options = new HashMap<>();
        if (value == null)
            options.remove(key);
        else
            options.put(key, value);
        return this;
    }

    @Override
    public Optional<String> option(String key) {
        if (options == null)
            return Optional.empty();
        return Optional.ofNullable(options.get(key));
    }

    public SsrTemplate dataRow(DataRow dataRow) {
        this.dataRow = dataRow;
        return this;
    }

    public DataRow dataRow() {
        return dataRow;
    }

    public SsrTemplate dataSet(DataSet dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public DataSet dataSet() {
        return dataSet;
    }

}
