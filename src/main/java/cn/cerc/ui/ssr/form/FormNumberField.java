package cn.cerc.ui.ssr.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.ISsrOption;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierList;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("输入框组件")
@VuiCommonComponent
public class FormNumberField extends VuiControl implements ISupportField {
    private static final ClassConfig FieldConfig = new ClassConfig(AbstractField.class, SummerUI.ID);
    private SsrBlock block = new SsrBlock();
    private String fieldDialogIcon;
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    Binder<ISupplierList> listSource = new Binder<>(this, ISupplierList.class);
    @Column
    String mark = "";
    @Column(name = "输入提示")
    String placeholder = "";
    @Column
    String dialog = "";
    @Column
    String pattern = "[\\d\\.]+";
    /**
     * 步进值
     * https://developer.mozilla.org/zh-CN/docs/Web/HTML/Element/input/number#step
     * <br>
     * 如果要输入任意个小数位，赋值为 any
     */
    @Column
    String step = "any";
    @Column
    boolean readonly = false;
    @Column
    boolean required = false;
    @Column
    boolean autofocus = false;

    public FormNumberField() {
        super();
        init();
    }

    public FormNumberField(String title, String field) {
        super();
        init();
        this.setId(title);
        this.field = field;
        this.title = title;
    }

    private void init() {
        block.option("_isTextField", "1");
        block.option("_addAll", "");
        var context = Application.getContext();
        if (context != null) {
            var impl = Application.getBean(ImageConfigImpl.class);
            if (impl != null) {
                this.fieldDialogIcon = impl.getClassProperty(AbstractField.class, SummerUI.ID, "icon", "");
            } else {
                this.fieldDialogIcon = FieldConfig.getClassProperty("icon", "");
            }
        }
    }

    @Override
    public SsrBlock request(ISsrBoard form) {
        String title = this.title;
        String field = this.field;

        var fieldKey = String.format("${%s}", field);
        if (Utils.isEmpty(field))
            fieldKey = "";
        block.id(title).fields(field).display(1);
        form.addBlock(title,
                block.text(String.format(
                        """
                                <li ${if _style}style='${_style}'${endif}>
                                    <label for="${fields}" ${if _mark}class='formMark' ${endif}>${if _required}<font role="require">*</font>${endif}<em>${_title}</em></label>
                                    <div>
                                        ${if _isTextField}
                                            <input type="number" step="${_step}" name="${fields}" id="${fields}" value="%s" autocomplete="off" ${if _readonly}readonly ${endif}${if _autofocus}autofocus ${endif}
                                            ${if _placeholder}placeholder="${_placeholder}" ${else}placeholder="请${if _dialog}点击获取${else}输入${endif}${_title}" ${endif}${if _pattern}pattern="${_pattern}" ${endif}${if _required}required ${endif}/>
                                        ${else}
                                            <select id="${fields}" name="${fields}" ${if _readonly}disabled ${endif}>
                                            ${if _addAll}<option value="">全部</option>${endif}
                                            ${list.begin}<option value="${list.index}" ${if list.index==%s}selected ${endif}>${list.value}</option>${list.end}
                                            </select>
                                        ${endif}
                                        <span role="suffix-icon">${if _dialog}<a href="javascript:${_dialog}"><img src="%s" /></a>${endif}</span>
                                    </div>
                                </li>
                                ${if _mark}
                                <li role="${fields}" class="liWord" style="display: none;">
                                    <mark>${_mark}</mark>
                                </li>
                                ${endif}
                                """,
                        fieldKey, field, fieldDialogIcon)));

        block().option("_readonly", this.readonly ? "1" : "");
        block().option("_required", this.required ? "1" : "");
        block().option("_mark", this.mark);
        block.fields(this.field);
        block.option("_title", title);
        block.option("_placeholder", this.placeholder);
        block.option(ISsrOption.Readonly, this.readonly ? "1" : "");
        block.option("_required", this.required ? "1" : "");
        block.option("_dialog", this.dialog);
        block.option("_mark", this.mark);
        block.option("_patten", this.pattern);
        block.option("_readonly", this.readonly ? "1" : "");
        block.option("_pattern", this.pattern);
        block.option("_autofocus", this.autofocus ? "1" : "");
        block.option("_style", this.properties("v_style").orElse(""));
        block.option("_step", this.step());
        return block;
    }

    @Override
    public void output(HtmlWriter html) {
        VuiForm form = this.findOwner(VuiForm.class);
        if (form != null && !form.columns().contains(title))
            return;
        html.print(block.html());
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.listSource.init();
            var form = this.findOwner(VuiForm.class);
            if (form != null) {
                this.request(form);
                form.addColumn(title);
            }
            break;
        case SsrMessage.InitListSourceDone:
            Optional<ISupplierList> optList = this.listSource.target();
            if (optList.isPresent()) {
                ISupplierList source = optList.get();
                block.toList(source.items());
                source.selected().ifPresent(selected -> block.dataRow().setValue(field, selected));
                block.option("_isTextField", "");
                block.option("_addAll", source.addAll() ? "1" : "");
            }
            break;
        }
    }

    public FormNumberField dialog(String... dialogFunc) {
        this.dialog = getDialogText(this.field, dialogFunc);
        return this;
    }

    public FormNumberField placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public FormNumberField readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public FormNumberField required(boolean required) {
        this.required = required;
        return this;
    }

    public FormNumberField autofocus(boolean autofocus) {
        this.autofocus = autofocus;
        return this;
    }

    public FormNumberField pattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public FormNumberField mark(String mark) {
        this.mark = mark;
        return this;
    }

    protected String getDialogText(String field, String... dialogFunc) {
        var sb = new StringBuffer();
        sb.append(dialogFunc[0]);
        sb.append("('").append(field).append("'");
        if (dialogFunc.length > 1) {
            for (var i = 1; i < dialogFunc.length; i++)
                sb.append(",'").append(dialogFunc[i]).append("'");
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public FormNumberField title(String title) {
        this.title = title;
        return this;
    }

    public FormNumberField url(Supplier<String> url) {
        block.option("_enabled_url", "1");
        block.onCallback("_url", url);
        return this;
    }

    @Override
    public void saveEditor(RequestReader reader) {
        var oldValue = this.field;
        if (oldValue == null)
            oldValue = "";
        super.saveEditor(reader);
        var newValue = this.field;
        if (!oldValue.equals(newValue))
            this.canvas().sendMessage(this, SsrMessage.RenameFieldCode, newValue, this.getOwner().getId());
    }

    @Override
    public String getIdPrefix() {
        return "field";
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public SsrBlock block() {
        return this.block;
    }

    @Override
    public String fields() {
        return this.field;
    }

    @Override
    public FormNumberField field(String field) {
        this.field = field;
        return this;
    }

    public FormNumberField toList(List<String> targetList) {
        block.toList(targetList);
        block.option("_isTextField", "");
        return this;
    }

    public FormNumberField toList(Enum<?>[] enums) {
        List<String> list = new ArrayList<>();
        for (Enum<?> item : enums)
            list.add(item.name());
        return toList(list);
    }

    public String step() {
        return step;
    }

    public FormNumberField step(String step) {
        this.step = step;
        return this;
    }

}
