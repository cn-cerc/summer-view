package cn.cerc.ui.ssr.form;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.fields.DateField;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("日期时间组件")
@VuiCommonComponent
public class FormDatetimeField extends VuiControl implements ISupportField {
    private static final ClassConfig DateConfig = new ClassConfig(DateField.class, SummerUI.ID);
    private SsrBlock block = new SsrBlock();
    private String dateDialogIcon;
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    String pattern = "";
    @Column
    String placeholder = "";
    @Column
    boolean required = false;
    @Column
    boolean readonly = false;
    @Column
    boolean autofocus = false;
    @Column
    DatetimeKindEnum kind = DatetimeKindEnum.Datetime;

    public FormDatetimeField() {
        super();
        init();
    }

    public FormDatetimeField(String title, String field) {
        this.title = title;
        this.field = field;
        block.id(title).fields(field);
        init();
    }

    private void init() {
        var context = Application.getContext();
        if (context != null) {
            var impl = Application.getBean(ImageConfigImpl.class);
            if (impl != null)
                this.dateDialogIcon = impl.getClassProperty(DateField.class, SummerUI.ID, "icon", "");
            else
                this.dateDialogIcon = DateConfig.getClassProperty("icon", "");
        }
        block.display(1);
    }

    @Override
    public SsrBlock block() {
        return block;
    }

    @Override
    public SsrBlock request(ISsrBoard form) {
        String title = this.title;
        String field = this.field;
        String dialog = switch (kind) {
        case Datetime -> "showDateTimeDialog";
        case OnlyDate -> "showDateDialog";
        case YearMonth -> "showYMDialog";
        default -> "showDateTimeDialog";
        };
        form.addBlock(title,
                block.text(String.format(
                        """
                                <li ${if _style}style='${_style}'${endif}>
                                    <label for="%s"><em>%s</em></label>
                                    <div>
                                        <input type="text" name="%s" id="%s" value="${%s}" autocomplete="off" ${if _readonly}readonly ${endif}${if _autofocus}autofocus ${endif}
                                        ${if _placeholder}placeholder="${_placeholder}" ${else}placeholder="请点击获取%s" ${endif}${if _pattern}pattern="${_pattern}" ${endif}${if _required}required ${endif}/>
                                        <span role="suffix-icon">
                                            <a href="javascript:%s('%s')">
                                                <img src="%s" />
                                            </a>
                                        </span>
                                    </div>
                                </li>
                                """,
                        field, title, field, field, field, title, dialog, field, dateDialogIcon)));
        block().option("_placeholder", this.placeholder);
        block().option("_readonly", this.readonly ? "1" : "");
        block().option("_required", this.required ? "1" : "");
        block().option("_autofocus", this.autofocus ? "1" : "");
        block().option("_pattern", this.pattern);
        block().option("_style", this.properties("v_style").orElse(""));
        block.id(title).fields(this.field);
        return block;
    }

    @Override
    public void output(HtmlWriter html) {
        VuiForm form = this.findOwner(VuiForm.class);
        if (form != null && !form.columns().contains(title))
            return;
        html.print(block.html());
    }

    public FormDatetimeField placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public FormDatetimeField readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public FormDatetimeField required(boolean required) {
        this.required = required;
        return this;
    }

    @Deprecated
    public FormDatetimeField patten(String pattern) {
        return pattern(pattern);
    }

    public FormDatetimeField pattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public FormDatetimeField title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String fields() {
        return this.field;
    }

    @Override
    public FormDatetimeField field(String field) {
        this.field = field;
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

    public DatetimeKindEnum getKind() {
        return kind;
    }

    public FormDatetimeField setKind(DatetimeKindEnum kind) {
        this.kind = kind;
        return this;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitBinder:
            var form = this.findOwner(VuiForm.class);
            if (form != null) {
                this.request(form);
                form.addColumn(title);
            }
            break;
        }
    }
}