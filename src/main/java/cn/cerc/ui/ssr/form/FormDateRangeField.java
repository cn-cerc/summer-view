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
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.SsrControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("日期块组件")
public class FormDateRangeField extends SsrControl implements ISupportForm {
    private static final ClassConfig DateConfig = new ClassConfig(DateField.class, SummerUI.ID);
    private SsrBlock block = new SsrBlock();
    private String dateDialogIcon;
    @Column
    String title = "";
    @Column
    String beginField = "";
    @Column
    String endField = "";
    @Column
    String patten = "";
    @Column
    String placeholder = "";
    @Column
    boolean required = false;
    @Column
    boolean readonly = false;

    public FormDateRangeField() {
        super();
        init();
    }

    public FormDateRangeField(String title, String beginDate, String endDate) {
        this.title = title;
        this.beginField = beginDate;
        this.endField = endDate;
        block.id(title).fields(String.format("%s,%s", beginDate, endDate)).display(1);
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
    public SsrBlock block() {
        return block;
    }

    @Override
    public SsrBlock request(ISsrBoard form) {
        String title = this.title;
        String beginDate = this.beginField;
        String endDate = this.endField;
        form.addBlock(title, block.templateText(String.format("""
                    <li>
                    <label for="start_date_"><em>%s</em></label>
                    <div class="dateArea">
                        <input autocomplete="off" name="%s" id="%s" type="text" class="dateAreaInput" value="${%s}"
                        ${if _pattern}pattern="${_pattern}"${endif} ${if _required}required${endif}
                        ${if _placeholder}placeholder="${_placeholder}"${endif} />
                        <span>/</span>
                        <input autocomplete="off" name="%s" id="%s" type="text" class="dateAreaInput" value="${%s}"
                        ${if _pattern}pattern="${_pattern}"${endif} ${if _required}required${endif}
                        ${if _placeholder}placeholder="${_placeholder}"${endif} />
                        <span role="suffix-icon">
                            <a href="javascript:showDateAreaDialog('%s', '%s')">
                            <img src="%s" />
                            </a>
                        </span>
                    </div>
                </li>
                """, title, beginDate, beginDate, beginDate, endDate, endDate, endDate, beginDate, endDate,
                dateDialogIcon)));
        block().option("_placeholder", this.placeholder);
        block().option("_readonly", this.readonly ? "1" : "");
        block().option("_required", this.required ? "1" : "");
        block().option("_patten", this.patten);
        block.fields(String.format("%s,%s", this.beginField, this.endField));
        return block;
    }

    public FormDateRangeField placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public FormDateRangeField readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public FormDateRangeField required(boolean required) {
        this.required = required;
        return this;
    }

    public FormDateRangeField patten(String patten) {
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
        return String.format("%s,%s", this.beginField, this.endField);
    }

    @Override
    public ISupportForm field(String fields) {
        String[] fieldsArray = fields.split(",");
        if (fieldsArray.length == 2) {
            this.beginField = fieldsArray[0];
            this.endField = fieldsArray[1];
        }
        return this;
    }

    @Override
    public boolean saveEditor(RequestReader reader) {
        var oldBeginValue = this.beginField;
        if (oldBeginValue == null)
            oldBeginValue = "";
        var oldEndValue = this.endField;
        if (oldEndValue == null)
            oldEndValue = "";
        var result = super.saveEditor(reader);
        var beginValue = this.beginField;
        var endValue = this.endField;
        if (!oldBeginValue.equals(beginValue))
            this.getContainer().sendMessage(this, SsrMessage.RenameFieldCode, beginValue, this.getOwner().getId());
        if (!oldEndValue.equals(endValue))
            this.getContainer().sendMessage(this, SsrMessage.RenameFieldCode, endValue, this.getOwner().getId());
        return result;
    }

    @Override
    public String getIdPrefix() {
        return "field";
    }

}