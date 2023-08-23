package cn.cerc.ui.ssr.form;

import java.util.Map;
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
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.ISsrOption;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;
import cn.cerc.ui.ssr.source.Binder;
import cn.cerc.ui.ssr.source.ISupplierMap;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("输入框组件")
public class FormStringField extends VuiControl implements ISupportForm {
    private static final ClassConfig FieldConfig = new ClassConfig(AbstractField.class, SummerUI.ID);
    private SsrBlock block = new SsrBlock();
    private String fieldDialogIcon;
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    Binder<ISupplierMap> mapSource = new Binder<>(this, ISupplierMap.class);
    @Column
    String mark = "";
    @Column(name = "输入提示")
    String placeholder = "";
    @Column
    String dialog = "";
    @Column
    String patten = "";
    @Column
    boolean readonly = false;
    @Column
    boolean required = false;
    @Column
    boolean autofocus = false;

    public FormStringField() {
        super();
        init();
    }

    public FormStringField(String title, String field) {
        super();
        init();
        this.setId(title);
        this.field = field;
        this.title = title;
    }

    private void init() {
        block.option("_isTextField", "1");
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

        block.id(title).fields(field).display(1);
        var fieldKey = String.format("${%s}", field);
        if (Utils.isEmpty(field))
            fieldKey = "";
        form.addBlock(title,
                block.text(String.format(
                        """
                                <li ${if _style}style='${_style}'${endif}>
                                    <label for="${fields}"${if _mark} class='formMark'${endif}>${if _required}<font role="require">*</font>${endif}<em>${_title}</em></label>
                                    <div>
                                        ${if _isTextField}
                                            <input type="text" name="${fields}" id="${fields}" value="%s" autocomplete="off"${if _readonly} readonly${endif}${if _autofocus} autofocus${endif}
                                            ${if _placeholder} placeholder="${_placeholder}"${else} placeholder="请${if _dialog}点击获取${else}输入${endif}${_title}"${endif}${if _pattern} pattern="${_pattern}"${endif}${if _required} required${endif} />
                                        ${else}
                                            <select id="${fields}" name="${fields}"${if _readonly} disabled${endif}>
                                            ${map.begin}<option value="${map.key}" ${if map.key==%s}selected${endif}>${map.value}</option>${map.end}
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
        block.option("_patten", this.patten);
        block.option("_readonly", this.readonly ? "1" : "");
        block.option("_pattern", this.patten);
        block.option("_autofocus", this.autofocus ? "1" : "");
        block.option("_style", this.properties("v_style").orElse(""));
        return block;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            this.mapSource.init();
            break;
        case SsrMessage.InitMapSourceDone:
            Optional<ISupplierMap> optMap = this.mapSource.target();
            if (optMap.isPresent()) {
                ISupplierMap source = optMap.get();
                source.selected().ifPresent(this::selected);
                block.toMap(source.items());
                block.option("_isTextField", "");
            }
            break;
        }
    }

    public FormStringField dialog(String... dialogFunc) {
        this.dialog = getDialogText(this.field, dialogFunc);
        return this;
    }

    public FormStringField placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public FormStringField readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public FormStringField required(boolean required) {
        this.required = required;
        return this;
    }

    public FormStringField autofocus(boolean autofocus) {
        this.autofocus = autofocus;
        return this;
    }

    public FormStringField patten(String patten) {
        this.patten = patten;
        return this;
    }

    public FormStringField mark(String mark) {
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

    public Optional<String> selected() {
        return block.option("_selected");
    }

    public FormStringField selected(String selected) {
        block.option("_selected", selected);
        return this;
    }

    @Override
    public FormStringField title(String title) {
        this.title = title;
        return this;
    }

    public FormStringField url(Supplier<String> url) {
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
    public ISupportForm field(String field) {
        this.field = field;
        return this;
    }

    public FormStringField toMap(String key, String val) {
        block.toMap(key, val);
        block.option("_isTextField", "");
        return this;
    }

    public ISupplierBlock toMap(Map<String, String> targetMap) {
        block.toMap(targetMap);
        block.option("_isTextField", "");
        return this;
    }

}
