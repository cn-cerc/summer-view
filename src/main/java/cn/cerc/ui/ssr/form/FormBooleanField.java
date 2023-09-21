package cn.cerc.ui.ssr.form;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("开关组件")
@VuiCommonComponent
public class FormBooleanField extends VuiControl implements ISupportField {
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
        block.display(1);
    }

    public FormBooleanField(String title, String field) {
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
        form.addBlock(title,
                block.text(String.format(
                        """
                                    <li ${if _style}style='${_style}'${endif}>
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
        block.option("_style", this.properties("v_style").orElse(""));
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

    public FormBooleanField mark(String mark) {
        this.mark = mark;
        return this;
    }

    @Override
    public String title() {
        return this.title;
    }

    @Override
    public FormBooleanField title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String fields() {
        return this.field;
    }

    @Override
    public FormBooleanField field(String field) {
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