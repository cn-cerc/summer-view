
package cn.cerc.ui.ssr;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.fields.DateField;
import cn.cerc.ui.fields.ImageConfigImpl;

public class SsrFormStyleDefault implements SsrFormStyleImpl {
    private static final ClassConfig FieldConfig = new ClassConfig(AbstractField.class, SummerUI.ID);
    private static final ClassConfig DateConfig = new ClassConfig(DateField.class, SummerUI.ID);

    private List<String> items = new ArrayList<>();

    private String fieldDialogIcon;
    private String dateDialogIcon;
    private ImageConfigImpl imageConfig;

    public SsrFormStyleDefault() {
        var impl = Application.getBean(ImageConfigImpl.class);
        if (impl != null) {
            this.fieldDialogIcon = impl.getClassProperty(AbstractField.class, SummerUI.ID, "icon", "");
            this.dateDialogIcon = impl.getClassProperty(DateField.class, SummerUI.ID, "icon", "");
        } else {
            this.fieldDialogIcon = FieldConfig.getClassProperty("icon", "");
            this.dateDialogIcon = DateConfig.getClassProperty("icon", "");
        }

    }

    protected String getImage(String imgSrc) {
        if (imageConfig == null)
            imageConfig = Application.getBean(ImageConfigImpl.class);
        return imageConfig == null ? imgSrc : imageConfig.getCommonFile(imgSrc);
    }

    @Override
    public SupplierBlockImpl getSubmitButton() {
        return form -> {
            var ssr = form.addBlock(UISsrForm.FormStart, String.format("""
                            <div>
                            <span onclick="toggleSearch(this)">查询条件</span>
                            <div class="searchFormButtonDiv">
                                <button name="submit" value="search">查询</button>${if templateId}
                                <a role="configTemplate" type="button" onclick="showSsrConfigDialog('${templateId}')">
                                    <img class="default" src="%s" />
                                    <img class="hover" src="%s" />
                                </a>${endif}
                            </div>
                        </div>
                    """, getImage("images/icon/templateConfig.png"), getImage("images/icon/templateConfig_hover.png")));
            ssr.option("action", "").option("role", "search").option("templateId", "");
            return ssr;
        };
    }

    public class SupplierStringFormField implements SupplierStringFormFieldImpl {
        private SsrBlock block = new SsrBlock();
        private String title;
        private String field;

        public SupplierStringFormField(String title, String field) {
            this.title = title;
            this.field = field;
            block.id(title).fields(field).display(1);
        }

        @Override
        public SsrBlockImpl block() {
            return block;
        }

        @Override
        public SsrBlockImpl request(SsrComponentImpl form) {
            form.addBlock(title,
                    block.templateText(String.format(
                            """
                                    <li>
                                        <label for="%s"${if _mark} class='formMark'${endif}>${if required}<font role="require">*</font>${endif}<em>%s</em></label>
                                        <div>
                                            <input type="text" name="%s" id="%s" value="${%s}" autocomplete="off"${if _readonly} readonly${endif}${if _autofocus} autofocus${endif}
                                            ${if _placeholder} placeholder="${_placeholder}"${else} placeholder="请${if _dialog}点击获取${else}输入${endif}%s"${endif}${if _pattern} pattern="${_pattern}"${endif}${if _required} required${endif} />
                                            <span role="suffix-icon">${if _dialog}<a href="javascript:${_dialog}">
                                                    <img src="%s" />
                                                </a>${endif}</span>
                                        </div>
                                    </li>
                                    ${if _mark}
                                    <li role="%s" class="liWord" style="display: none;">
                                        <mark>${_mark}</mark>
                                    </li>
                                    ${endif}
                                    """,
                            field, title, field, field, field, title, fieldDialogIcon, field)));
            return block;
        }

        public SupplierStringFormField dialog(String... dialogFunc) {
            block.option("_dialog", getDialogText(field, dialogFunc));
            return this;
        }

        @Override
        public SupplierStringFormField placeholder(String placeholder) {
            SupplierStringFormFieldImpl.super.placeholder(placeholder);
            return this;
        }

        @Override
        public SupplierStringFormField readonly(boolean readonly) {
            SupplierStringFormFieldImpl.super.readonly(readonly);
            return this;
        }

        @Override
        public SupplierStringFormField required(boolean required) {
            SupplierStringFormFieldImpl.super.required(required);
            return this;
        }

        @Override
        public SupplierStringFormField autofocus(boolean autofocus) {
            SupplierStringFormFieldImpl.super.autofocus(autofocus);
            return this;
        }

        @Override
        public SupplierStringFormField patten(String patten) {
            SupplierStringFormFieldImpl.super.patten(patten);
            return this;
        }

        @Override
        public SupplierStringFormField mark(String mark) {
            SupplierStringFormFieldImpl.super.mark(mark);
            return this;
        }

    }

    @Override
    public SupplierStringFormField getString(String title, String field) {
        return new SupplierStringFormField(title, field);
    }

    public class SupplierBooleanFormField implements SupplierBooleanFormImpl {
        private SsrBlock block = new SsrBlock();
        private String title;
        private String field;

        public SupplierBooleanFormField(String title, String field) {
            this.title = title;
            this.field = field;
            block.id(title).fields(field).display(1);
        }

        @Override
        public SsrBlockImpl block() {
            return block;
        }

        @Override
        public SsrBlockImpl request(SsrComponentImpl form) {
            form.addBlock(title,
                    block.templateText(String.format(
                            """
                                        <li>
                                        <div role="switch">
                                            <input autocomplete="off" name="%s" id="%s" type="checkbox" value="1" ${if %s}checked ${endif}/>
                                        </div>
                                        <label for="%s"${if _mark} class='formMark'${endif}><em>%s</em></label>
                                    </li>
                                            """,
                            field, field, field, field, title)));
            return block;
        }

        @Override
        public SupplierBooleanFormField mark(String mark) {
            SupplierBooleanFormImpl.super.mark(mark);
            return this;
        }

    }

    @Override
    public SupplierBooleanFormField getBoolean(String title, String field) {
        items.add(title);
        return new SupplierBooleanFormField(title, field);
    }

    public class SupplierMapFormField implements SupplierMapFormFieldImpl {
        private SsrBlock block = new SsrBlock();
        private String title;
        private String field;

        public SupplierMapFormField(String title, String field) {
            this.title = title;
            this.field = field;
            block.id(title).fields(field).display(1);
        }

        @Override
        public SsrBlockImpl block() {
            return block;
        }

        @Override
        public SsrBlockImpl request(SsrComponentImpl form) {
            form.addBlock(title,
                    block.templateText(String.format(
                            """
                                    <li>
                                        <label for="%s"${if _mark} class='formMark'${endif}>${if required}<font role="require">*</font>${endif}<em>%s</em></label>
                                        <div>
                                            <select id="%s" name="%s"${if _readonly} disabled${endif}>
                                            ${map.begin}
                                                <option value="${map.key}" ${if map.key==%s}selected${endif}>${map.value}</option>
                                            ${map.end}
                                            </select>
                                        </div>
                                    </li>
                                    ${if _mark}
                                    <li role="%s" class="liWord" style="display: none;">
                                        <mark>${_mark}</mark>
                                    </li>
                                    ${endif}
                                    """,
                            field, title, field, field, field, field)));
            return block;
        }

        @Override
        public SupplierMapFormField readonly(boolean readonly) {
            SupplierMapFormFieldImpl.super.readonly(readonly);
            return this;
        }

        @Override
        public SupplierMapFormField required(boolean required) {
            SupplierMapFormFieldImpl.super.required(required);
            return this;
        }

        @Override
        public SupplierMapFormField mark(String mark) {
            SupplierMapFormFieldImpl.super.mark(mark);
            return this;
        }

    }

    @Override
    public SupplierMapFormField getMap(String title, String field) {
        items.add(title);
        return new SupplierMapFormField(title, field);
    }

    public class SupplierCodeNameField implements SupplierStringFormFieldImpl {

        private SsrBlock block = new SsrBlock();
        private String title;
        private String field;

        public SupplierCodeNameField(String title, String field, String... dialogFunc) {
            this.title = title;
            this.field = field;
            block.option("_dialog", getDialogText(String.format("%s,%s_name", field, field), dialogFunc)).display(1);
            block.id(title).fields(String.format("%s,%s_name", field, field)).display(1);
        }

        @Override
        public SsrBlockImpl block() {
            return block;
        }

        @Override
        public SsrBlockImpl request(SsrComponentImpl form) {
            form.addBlock(title,
                    block.templateText(String.format(
                            """
                                    <li>
                                        <label for="%s_name"><em>%s</em></label>
                                        <div>
                                            <input type="hidden" name="%s" id="%s" value="${%s}">
                                            <input type="text" name="%s_name" id="%s_name" value="${%s_name}" autocomplete="off" placeholder="请点击获取%s"${if _readonly} readonly${endif}>
                                            <span role="suffix-icon">
                                                <a href="javascript:${_dialog}">
                                                    <img src="%s">
                                                </a>
                                            </span>
                                        </div>
                                    </li>
                                    """,
                            field, title, field, field, field, field, field, field, title, fieldDialogIcon)));
            return block;
        }

        @Override
        public SupplierCodeNameField placeholder(String placeholder) {
            SupplierStringFormFieldImpl.super.placeholder(placeholder);
            return this;
        }

        @Override
        public SupplierCodeNameField readonly(boolean readonly) {
            SupplierStringFormFieldImpl.super.readonly(readonly);
            return this;
        }

        @Override
        public SupplierCodeNameField required(boolean required) {
            SupplierStringFormFieldImpl.super.required(required);
            return this;
        }

        @Override
        public SupplierCodeNameField autofocus(boolean autofocus) {
            SupplierStringFormFieldImpl.super.autofocus(autofocus);
            return this;
        }

        @Override
        public SupplierCodeNameField patten(String patten) {
            SupplierStringFormFieldImpl.super.patten(patten);
            return this;
        }

    }

    @Override
    public SupplierCodeNameField getCodeName(String title, String field, String... dialogFunc) {
        items.add(title);
        return new SupplierCodeNameField(title, field, dialogFunc);
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

    public class SupplierFastDateFormField implements SupplierFastDateFormFieldImpl {
        private SsrBlock block = new SsrBlock();
        private String title;
        private String field;
        // TODO 等dialog方法移除之后需要将此属性移除掉
        private String dialog = "showDateDialog";

        public SupplierFastDateFormField(String title, String field) {
            this.title = title;
            this.field = field;
            block.id(title).fields(field).display(1);
        }

        @Override
        public SsrBlockImpl block() {
            return block;
        }

        @Override
        public SsrBlockImpl request(SsrComponentImpl form) {
            form.addBlock(title,
                    block.templateText(String.format(
                            """
                                    <li>
                                        <label for="%s"><em>%s</em></label>
                                        <div>
                                            <input type="text" name="%s" id="%s" value="${%s}" autocomplete="off"${if _readonly} readonly${endif}${if _autofocus} autofocus${endif}
                                            ${if _placeholder} placeholder="${_placeholder}"${else} placeholder="请${if _dialog}点击获取${else}输入${endif}%s"${endif}${if _pattern} pattern="${_pattern}"${endif}${if _required} required${endif} />
                                            <span role="suffix-icon"><a href="javascript:%s('%s')">
                                                    <img src="%s" />
                                                </a></span>
                                        </div>
                                    </li>
                                    """,
                            field, title, field, field, field, title, dialog, field, dateDialogIcon)));
            return block;
        }

        @Override
        public SupplierFastDateFormField placeholder(String placeholder) {
            SupplierFastDateFormFieldImpl.super.placeholder(placeholder);
            return this;
        }

        @Override
        public SupplierFastDateFormField readonly(boolean readonly) {
            SupplierFastDateFormFieldImpl.super.readonly(readonly);
            return this;
        }

        @Override
        public SupplierFastDateFormField required(boolean required) {
            SupplierFastDateFormFieldImpl.super.required(required);
            return this;
        }

        @Override
        public SupplierFastDateFormField patten(String patten) {
            SupplierFastDateFormFieldImpl.super.patten(patten);
            return this;
        }

        @Deprecated
        public SupplierFastDateFormField dialog(String dialog) {
            this.dialog = dialog;
            return this;
        }

    }

    @Override
    public SupplierFastDateFormField getDate(String title, String field) {
        items.add(title);
        return new SupplierFastDateFormField(title, field);
    }

    public class SupplierDatetimeFormField implements SupplierDatetimeFormImpl {
        private SsrBlock block = new SsrBlock();
        private String title;
        private String field;

        public SupplierDatetimeFormField(String title, String field) {
            this.title = title;
            this.field = field;
            block.id(title).fields(field).display(1);
        }

        @Override
        public SsrBlockImpl block() {
            return block;
        }

        @Override
        public SsrBlockImpl request(SsrComponentImpl form) {
            form.addBlock(title,
                    block.templateText(String.format(
                            """
                                    <li>
                                        <label for="%s"><em>%s</em></label>
                                        <div>
                                            <input type="text" name="%s" id="%s" value="${%s}" autocomplete="off"${if _readonly} readonly${endif}${if _autofocus} autofocus${endif}
                                            ${if _placeholder} placeholder="${_placeholder}"${else} placeholder="请${if _dialog}点击获取${else}输入${endif}%s"${endif}${if _pattern} pattern="${_pattern}"${endif}${if _required} required${endif} />
                                            <span role="suffix-icon"><a href="javascript:showDateTimeDialog('%s')">
                                                    <img src="%s" />
                                                </a></span>
                                        </div>
                                    </li>
                                    """,
                            field, title, field, field, field, title, field, dateDialogIcon)));
            return block;
        }

        @Override
        public SupplierDatetimeFormField placeholder(String placeholder) {
            SupplierDatetimeFormImpl.super.placeholder(placeholder);
            return this;
        }

        @Override
        public SupplierDatetimeFormField readonly(boolean readonly) {
            SupplierDatetimeFormImpl.super.readonly(readonly);
            return this;
        }

        @Override
        public SupplierDatetimeFormField required(boolean required) {
            SupplierDatetimeFormImpl.super.required(required);
            return this;
        }

        @Override
        public SupplierDatetimeFormField patten(String patten) {
            SupplierDatetimeFormImpl.super.patten(patten);
            return this;
        }

    }

    @Override
    public SupplierDatetimeFormField getDatetime(String title, String field) {
        items.add(title);
        return new SupplierDatetimeFormField(title, field);
    }

    public class SupplierDateRangeFormField implements SupplierFastDateFormFieldImpl {
        private SsrBlock block = new SsrBlock();
        private String title;
        private String beginDate;
        private String endDate;

        public SupplierDateRangeFormField(String title, String beginDate, String endDate) {
            this.title = title;
            this.beginDate = beginDate;
            this.endDate = endDate;
            block.id(title).fields(String.format("%s,%s", beginDate, endDate)).display(1);
        }

        @Override
        public SsrBlockImpl block() {
            return block;
        }

        @Override
        public SsrBlockImpl request(SsrComponentImpl form) {
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
            return block;
        }

        @Override
        public SupplierDateRangeFormField placeholder(String placeholder) {
            SupplierFastDateFormFieldImpl.super.placeholder(placeholder);
            return this;
        }

        @Override
        public SupplierDateRangeFormField readonly(boolean readonly) {
            SupplierFastDateFormFieldImpl.super.readonly(readonly);
            return this;
        }

        @Override
        public SupplierDateRangeFormField required(boolean required) {
            SupplierFastDateFormFieldImpl.super.required(required);
            return this;
        }

        @Override
        public SupplierDateRangeFormField patten(String patten) {
            SupplierFastDateFormFieldImpl.super.patten(patten);
            return this;
        }
    }

    @Override
    public SupplierDateRangeFormField getDateRange(String title, String beginDate, String endDate) {
        items.add(title);
        return new SupplierDateRangeFormField(title, beginDate, endDate);
    }

    public class SupplierTextareaFormField implements SupplierTextareaFormFieldImpl {
        private SsrBlock block = new SsrBlock();
        private String title;
        private String field;

        public SupplierTextareaFormField(String title, String field) {
            this.title = title;
            this.field = field;
            block.id(title).fields(field).display(1);
        }

        @Override
        public SsrBlockImpl block() {
            return block;
        }

        @Override
        public SsrBlockImpl request(SsrComponentImpl form) {
            form.addBlock(title, block.templateText(String.format("""
                        <li>
                        <label for="%s">${if _required}<font role="require">*</font>${endif}<em>%s</em></label>
                        <div>
                            <textarea name="%s" id="%s">${%s}</textarea>
                            <span role="suffix-icon"></span>
                        </div>
                    </li>
                    """, field, title, field, field, field)));
            return block;
        }

        @Override
        public SupplierTextareaFormField placeholder(String placeholder) {
            SupplierTextareaFormFieldImpl.super.placeholder(placeholder);
            return this;
        }

        @Override
        public SupplierTextareaFormField readonly(boolean readonly) {
            SupplierTextareaFormFieldImpl.super.readonly(readonly);
            return this;
        }

        @Override
        public SupplierTextareaFormField required(boolean required) {
            SupplierTextareaFormFieldImpl.super.required(required);
            return this;
        }

        @Override
        public SupplierTextareaFormField mark(String mark) {
            SupplierTextareaFormFieldImpl.super.mark(mark);
            return this;
        }

    }

    @Override
    public SupplierTextareaFormField getTextarea(String title, String field) {
        items.add(title);
        return new SupplierTextareaFormField(title, field);
    }

    @Override
    public List<String> items() {
        return items;
    }

}
