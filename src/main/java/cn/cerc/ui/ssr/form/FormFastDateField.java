package cn.cerc.ui.ssr.form;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.fields.DateField;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.ISsrBlock;
import cn.cerc.ui.ssr.SsrBlock;
import cn.cerc.ui.ssr.core.SsrControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("日期组件")
public class FormFastDateField extends SsrControl implements ISupportForm {
    private static final ClassConfig DateConfig = new ClassConfig(DateField.class, SummerUI.ID);
    private SsrBlock block = new SsrBlock();
    private String dateDialogIcon;
    // TODO 等dialog方法移除之后需要将此属性移除掉
    private String dialog = "showDateDialog";
    @Column
    String title = "";
    @Column
    String field = "";
    @Column
    String placeholder = "";
    @Column
    String patten = "";
    @Column
    boolean required = false;
    @Column
    boolean readonly = false;

    public FormFastDateField() {
        super();
        init();
    }

    public FormFastDateField(String title, String field) {
        this.title = title;
        this.field = field;
        block.id(title).fields(field).display(1);
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
                                    <label for="%s"><em>%s</em></label>
                                    <div>
                                        <input type="text" name="%s" id="%s" value="${%s}" autocomplete="off"${if _readonly} readonly${endif}${if _autofocus} autofocus${endif}
                                        ${if _placeholder} placeholder="${_placeholder}"${else} placeholder="请点击获取%s"${endif}${if _pattern} pattern="${_pattern}"${endif}${if _required} required${endif} />
                                        <span role="suffix-icon"><a href="javascript:%s('%s')">
                                                <img src="%s" />
                                            </a></span>
                                    </div>
                                </li>
                                """,
                        field, title, field, field, field, title, dialog, field, dateDialogIcon)));
        block().option("_placeholder", this.placeholder);
        block().option("_readonly", this.readonly ? "1" : "");
        block().option("_required", this.required ? "1" : "");
        block().option("_patten", this.patten);
        block.fields(this.field);
        return block;
    }

    public FormFastDateField placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public FormFastDateField readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public FormFastDateField required(boolean required) {
        this.required = required;
        return this;
    }

    public FormFastDateField patten(String patten) {
        this.patten = patten;
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