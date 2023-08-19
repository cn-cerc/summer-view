package cn.cerc.ui.ssr.form;

import java.util.function.Supplier;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("文本域组件")
public class FormTextareaField extends SsrControl implements ISupportForm {
    private SsrBlock block = new SsrBlock();
    @Column
    String title;
    @Column
    String field;
    @Column
    String placeholder = "";
    @Column
    boolean readonly = false;
    @Column
    boolean required = false;

    public FormTextareaField() {
        super();
    }

    public FormTextareaField(String title, String field) {
        this.title = title;
        this.field = field;
        block.id(title).fields(field).display(1);
    }

    @Override
    public SsrBlock block() {
        return block;
    }

    @Override
    public SsrBlock request(ISsrBoard form) {
        String title = this.title;
        String field = this.field;
        form.addBlock(title, block.text(String.format("""
                    <li>
                    <label for="%s">${if _required}<font role="require">*</font>${endif}<em>%s</em></label>
                    <div>
                        <textarea name="%s" id="%s"${if _readonly} readonly${endif}>${%s}</textarea>
                        <span role="suffix-icon"></span>
                    </div>
                </li>
                """, field, title, field, field, field)));
        block().option("_placeholder", this.placeholder);
        block().option("_readonly", this.readonly ? "1" : "");
        block().option("_required", this.required ? "1" : "");
        block.fields(this.field);
        return block;
    }

    public FormTextareaField placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public FormTextareaField readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public FormTextareaField required(boolean required) {
        this.required = required;
        return this;
    }

    public FormTextareaField url(Supplier<String> url) {
        block().option("_enabled_url", "1");
        block().onCallback("_url", url);
        return this;
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public ISupportForm title(String title) {
        this.title = title;
        return this;
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

    @Override
    public boolean saveEditor(RequestReader reader) {
        var oldValue = this.field;
        if (oldValue == null)
            oldValue = "";
        var result = super.saveEditor(reader);
        var newValue = this.field;
        if (!oldValue.equals(newValue))
            this.getContainer().sendMessage(this, SsrMessage.RenameFieldCode, newValue, this.getOwner().getId());
        return result;
    }

    @Override
    public String getIdPrefix() {
        return "field";
    }

}