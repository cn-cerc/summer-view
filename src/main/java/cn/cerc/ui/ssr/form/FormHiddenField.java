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
@Description("隐藏输入框组件")
@VuiCommonComponent
public class FormHiddenField extends VuiControl implements ISupportField {
    private SsrBlock block = new SsrBlock();
    @Column
    String title = "";
    @Column
    String field = "";

    public FormHiddenField() {
        super();
    }

    public FormHiddenField(String title, String field) {
        super();
        this.setId(title);
        this.field = field;
        this.title = title;
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
    public SsrBlock request(ISsrBoard form) {
        block.id(title).fields(field).fixed(form);
        form.addBlock(title, block.text(String.format("""
                <input type="hidden"  name="${fields}" id="${fields}" value="${%s}" />
                """, field)));
        return block;
    }

    @Override
    public void onMessage(Object sender, int msgType, Object msgData, String targetId) {
        switch (msgType) {
        case SsrMessage.InitContent:
            VuiForm form = this.findOwner(VuiForm.class);
            if (form != null) {
                this.request(form);
                form.addColumn(title);
            }
            break;
        }
    }

    @Override
    public void output(HtmlWriter html) {
        html.print(block.html());
    }

    @Override
    public FormHiddenField title(String title) {
        this.title = title;
        return this;
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
    public FormHiddenField field(String field) {
        this.field = field;
        return this;
    }
}
