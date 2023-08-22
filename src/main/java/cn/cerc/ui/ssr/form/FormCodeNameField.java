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
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("代码名称块组件")
public class FormCodeNameField extends VuiControl implements ISupportForm {
    private static final ClassConfig FieldConfig = new ClassConfig(AbstractField.class, SummerUI.ID);
    private SsrBlock block = new SsrBlock();
    private String fieldDialogIcon;
    @Column
    String title;
    @Column
    String codeField;
    @Column
    String nameField;
    @Column
    String dialog = "";
    @Column
    boolean readonly = true;
    @Column
    private String placeholder = "";

    public FormCodeNameField() {
        super();
        init();
    }

    public FormCodeNameField(String title, String field, String... dialogFunc) {
        this.title = title;
        this.codeField = field;
        this.nameField = field + "_name";
        this.dialog = getDialogText(String.format("%s,%s_name", field, field), dialogFunc);
        block.id(title).fields(String.format("%s,%s_name", field, field)).display(1);
        init();
    }

    private void init() {
        var context = Application.getContext();
        if (context != null) {
            var impl = Application.getBean(ImageConfigImpl.class);
            if (impl != null)
                this.fieldDialogIcon = impl.getClassProperty(AbstractField.class, SummerUI.ID, "icon", "");
            else
                this.fieldDialogIcon = FieldConfig.getClassProperty("icon", "");
        }
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
    public void saveEditor(RequestReader reader) {
        var oldCodeValue = this.codeField;
        if (oldCodeValue == null)
            oldCodeValue = "";
        var oldNameValue = this.nameField;
        if (oldNameValue == null)
            oldNameValue = "";
        super.saveEditor(reader);
        var codeValue = this.codeField;
        var nameValue = this.nameField;
        if (!oldCodeValue.equals(codeValue))
            this.canvas().sendMessage(this, SsrMessage.RenameFieldCode, codeValue, this.getOwner().getId());
        if (!oldNameValue.equals(nameValue))
            this.canvas().sendMessage(this, SsrMessage.RenameFieldCode, nameValue, this.getOwner().getId());
    }

    @Override
    public SsrBlock block() {
        return block;
    }

    @Override
    public SsrBlock request(ISsrBoard form) {
        String title = this.title;
        String codeField = this.codeField;
        String nameField = this.nameField;
        form.addBlock(title,
                block.text(String.format(
                        """
                                <li ${if _style}style='${_style}'${endif}>
                                    <label for="%s"><em>%s</em></label>
                                    <div>
                                        <input type="hidden" name="%s" id="%s" value="${%s}">
                                        <input type="text" name="%s" id="%s" value="${%s}" autocomplete="off" placeholder="请点击获取%s"${if _readonly} readonly${endif}>
                                        <span role="suffix-icon">
                                            <a href="javascript:${_dialog}">
                                                <img src="%s">
                                            </a>
                                        </span>
                                    </div>
                                </li>
                                """,
                        nameField, title, codeField, codeField, codeField, nameField, nameField, nameField, title,
                        fieldDialogIcon)));
        block().option("_readonly", this.readonly ? "1" : "");
        block().option("_style", this.properties("v_style").orElse(""));
        block.option("_dialog", this.dialog);
        block.fields(fields());
        return block;
    }

    public FormCodeNameField readonly(boolean readonly) {
        this.readonly = readonly;
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
        return String.format("%s,%s", this.codeField, this.nameField);
    }

    @Override
    public ISupportForm field(String fields) {
        String[] fieldsArray = fields.split(",");
        if (fieldsArray.length == 2) {
            this.codeField = fieldsArray[0];
            this.nameField = fieldsArray[1];
        }
        return this;
    }

    @Override
    public String getIdPrefix() {
        return "field";
    }

    public FormCodeNameField placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

}