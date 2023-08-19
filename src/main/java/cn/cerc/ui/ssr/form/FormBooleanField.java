package cn.cerc.ui.ssr.form;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.ISsrBlock;
import cn.cerc.ui.ssr.core.SsrControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("开关组件")
public class FormBooleanField extends SsrControl implements ISupportForm {
    private SsrBlock block = new SsrBlock();
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    String mark = "";
    @Column
    boolean readonly = false;
    @Column
    boolean required = false;

    public FormBooleanField() {
        super();
    }

    public FormBooleanField(String title, String field) {
        this.title = title;
        this.field = field;
        block.id(title).fields(field).display(1);
    }

    @Override
    public ISsrBlock block() {
        return block;
    }

    @Override
    public ISsrBlock request(ISsrBoard form) {
        String title = this.title;
        String field = this.field;
        form.addBlock(title,
                block.templateText(String.format(
                        """
                                    <li>
                                    <div role="switch">
                                        <input autocomplete="off" name="%s" id="%s" type="checkbox" value="1" ${if %s}checked ${endif} ${if _readonly}disabled ${endif} />
                                    </div>
                                    <label for="%s"${if _mark} class='formMark'${endif}><em>%s</em></label>
                                </li>
                                        """,
                        field, field, field, field, title)));
        block().option("_mark", this.mark);
        block.option("_readonly", this.readonly ? "1" : "");
        block.option("_required", this.required ? "1" : "");
        block.fields(this.field);
        return block;
    }

    public FormBooleanField mark(String mark) {
        this.mark = mark;
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