package cn.cerc.ui.style;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cerc.db.core.DataRow;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.mis.core.IForm;
import cn.cerc.ui.core.UIComponent;

public class UISsrForm extends UIComponent {
    private static final Logger log = LoggerFactory.getLogger(UISsrForm.class);
    private SsrDefine define;
    private DataRow dataRow;
    private List<String> fields;
    public static final String FormBegin = "form.begin";
    public static final String FormEnd = "form.end";
    private Map<String, Consumer<SsrTemplateImpl>> onGetItem = new HashMap<>();

    public UISsrForm(UIComponent owner) {
        super(owner);
    }

    public UISsrForm(UIComponent owner, String templateText) {
        super(owner);
        define = new SsrDefine(templateText);
        init(findOwner(IForm.class));
    }

    public UISsrForm(UIComponent owner, Class<?> class1, String id) {
        super(owner);
        define = new SsrDefine(class1, id);
        init(findOwner(IForm.class));
    }

    private UISsrForm init(IForm form) {
        if (form != null) {
            for (var block : define.items().keySet())
                define.get(block).get().getOptions().put("isPhone", "" + form.getClient().isPhone());
        }
        return this;
    }

    public DataRow getDataRow() {
        return dataRow;
    }

    public void setDataRow(DataRow dataRow) {
        this.dataRow = dataRow;
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.dataRow == null) {
            log.error("dataRow is null");
            return;
        }
        if (this.fields == null)
            this.fields = this.dataRow.fields().names();

        addBlock(SsrDefine.BeginFlag).ifPresent(value -> html.print(value.getHtml()));
        // 输出内容
        addBlock(FormBegin, getDefault_FormBegin()).ifPresent(value -> html.print(value.getHtml()));
        for (var field : fields) {
            var block = addBlock(field, () -> new SsrTemplate(
                    String.format("%s: <input type=\"text\" name=\"%s\" value=\"${%s}\">", field, field, field)));
            if (block.isPresent()) {
                this.onGetItem.forEach((key, value) -> {
                    if (key.equals(field))
                        value.accept(block.get().setId(field));
                });
            }
            block.ifPresent(value -> html.print(value.getHtml()));
        }
        addBlock(FormEnd, () -> new SsrTemplate("</form>")).ifPresent(value -> html.print(value.getHtml()));
        addBlock(SsrDefine.EndFlag).ifPresent(value -> html.print(value.getHtml()));
    }

    public void addGetItem(String field, Consumer<SsrTemplateImpl> consumer) {
        this.onGetItem.put(field, consumer);
    }

    private Supplier<SsrTemplateImpl> getDefault_FormBegin() {
        return () -> new SsrTemplate("<form method='post'>");
    }

    private Optional<SsrTemplateImpl> addBlock(String id) {
        SsrTemplateImpl template = define.get(id).orElse(null);
        if (template != null) {
            template.setId(id);
            template.setDataRow(dataRow);
        }
        return Optional.ofNullable(template);
    }

    private Optional<SsrTemplateImpl> addBlock(String id, Supplier<SsrTemplateImpl> supplier) {
        SsrTemplateImpl template = define.getOrAdd(id, supplier).orElse(null);
        if (template != null) {
            template.setId(id);
            template.setDataRow(dataRow);
        } else
            log.error("表单模版中缺失定义：{}", id);
        return Optional.ofNullable(template);
    }

    public void addField(String... field) {
        if (fields == null)
            fields = new ArrayList<>();
        for (var item : field)
            fields.add(item);
    }

    public UISsrForm addField(String id, Consumer<SsrTemplateImpl> onGetItem) {
        this.addField(id);
        this.onGetItem.put(id, onGetItem);
        return this;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public SsrDefine getDefine() {
        return define;
    }

    public boolean readAll(HttpServletRequest request, String submitId) {
        var submit = request.getParameter(submitId);
        if (submit == null)
            return false;

        define.items().forEach((block, ssr) -> {
            var map = ssr.getMap();
            if (map != null) {
                var fields = ssr.getMap().get("fields");
                if (fields != null) {
                    for (var field : fields.split(","))
                        dataRow.setValue(field, request.getParameter(field));
                }
            }
        });
        return true;
    }

}
