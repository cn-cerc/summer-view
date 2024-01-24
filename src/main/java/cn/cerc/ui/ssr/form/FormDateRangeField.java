package cn.cerc.ui.ssr.form;

import javax.persistence.Column;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.db.core.Datetime;
import cn.cerc.db.core.Datetime.DateType;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.core.RequestReader;
import cn.cerc.ui.fields.DateField;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.DefaultDateRangeEnum;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.core.VuiCommonComponent;
import cn.cerc.ui.ssr.core.VuiControl;
import cn.cerc.ui.ssr.editor.ISsrBoard;
import cn.cerc.ui.ssr.editor.SsrMessage;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Description("日期块组件")
@VuiCommonComponent
public class FormDateRangeField extends VuiControl implements ISupportField {
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
    @Column
    DefaultDateRangeEnum range = DefaultDateRangeEnum.无;

    public FormDateRangeField() {
        super();
        init();
    }

    public FormDateRangeField(String title, String beginDate, String endDate) {
        this.title = title;
        this.beginField = beginDate;
        this.endField = endDate;
        block.id(title).fields(String.format("%s,%s", beginDate, endDate));
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
        String beginDate = this.beginField;
        String endDate = this.endField;
        form.addBlock(title, block.text(String.format("""
                    <li ${if _style}style='${_style}'${endif}>
                    <label title="%s" for="start_date_"><em>%s</em></label>
                    <div class="dateArea">
                        <input autocomplete="off" name="%s" id="%s" type="text" class="dateAreaInput" value="${%s}"
                        ${if _pattern}pattern="${_pattern}" ${endif}${if _required}required ${endif}
                        ${if _placeholder}placeholder="${_placeholder}" ${endif}/>
                        <span>/</span>
                        <input autocomplete="off" name="%s" id="%s" type="text" class="dateAreaInput" value="${%s}"
                        ${if _pattern}pattern="${_pattern}" ${endif}${if _required}required ${endif}
                        ${if _placeholder}placeholder="${_placeholder}" ${endif}/>
                        <span role="suffix-icon">
                            <a href="javascript:showDateAreaDialog('%s', '%s')">
                            <img src="%s" />
                            </a>
                        </span>
                    </div>
                </li>
                """, title, title, beginDate, beginDate, beginDate, endDate, endDate, endDate, beginDate, endDate,
                dateDialogIcon)));
        block().option("_placeholder", this.placeholder);
        block().option("_readonly", this.readonly ? "1" : "");
        block().option("_required", this.required ? "1" : "");
        block().option("_pattern", this.patten);
        block().option("_style", this.properties("v_style").orElse(""));
        block.id(title).fields(String.format("%s,%s", this.beginField, this.endField));
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
        case SsrMessage.InitBinder: {
            var form = this.findOwner(VuiForm.class);
            if (form != null) {
                this.request(form);
                form.addColumn(title);
            }
            break;
        }
        case SsrMessage.InitProperties:
            if (range != DefaultDateRangeEnum.无) {
                VuiForm form = findOwner(VuiForm.class);
                if (form != null) {
                    Datetime now = new Datetime();
                    switch (range) {
                    case 最近一周:
                        form.dataRow().setValue(beginField, now.inc(DateType.Day, -7).getDate());
                        break;
                    case 最近一个月:
                        form.dataRow().setValue(beginField, now.inc(DateType.Month, -1).getDate());
                        break;
                    case 最近三个月:
                        form.dataRow().setValue(beginField, now.inc(DateType.Month, -3).getDate());
                        break;
                    case 最近半年:
                        form.dataRow().setValue(beginField, now.inc(DateType.Month, -6).getDate());
                        break;
                    case 最近一年:
                        form.dataRow().setValue(beginField, now.inc(DateType.Year, -1).getDate());
                        break;
                    default:
                        break;
                    }
                    form.dataRow().setValue(endField, now.getDate());
                }
            }
            break;
        }
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
    public FormDateRangeField title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String fields() {
        return String.format("%s,%s", this.beginField, this.endField);
    }

    @Override
    public FormDateRangeField field(String fields) {
        String[] fieldsArray = fields.split(",");
        if (fieldsArray.length == 2) {
            this.beginField = fieldsArray[0];
            this.endField = fieldsArray[1];
        }
        return this;
    }

    @Override
    public void saveEditor(RequestReader reader) {
        var oldBeginValue = this.beginField;
        if (oldBeginValue == null)
            oldBeginValue = "";
        var oldEndValue = this.endField;
        if (oldEndValue == null)
            oldEndValue = "";
        super.saveEditor(reader);
        var beginValue = this.beginField;
        var endValue = this.endField;
        if (!oldBeginValue.equals(beginValue))
            this.canvas().sendMessage(this, SsrMessage.RenameFieldCode, beginValue, this.getOwner().getId());
        if (!oldEndValue.equals(endValue))
            this.canvas().sendMessage(this, SsrMessage.RenameFieldCode, endValue, this.getOwner().getId());
    }

    @Override
    public String getIdPrefix() {
        return "field";
    }

}