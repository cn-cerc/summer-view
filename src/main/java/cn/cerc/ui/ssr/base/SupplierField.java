package cn.cerc.ui.ssr.base;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.cerc.db.core.ClassConfig;
import cn.cerc.mis.core.Application;
import cn.cerc.ui.SummerUI;
import cn.cerc.ui.fields.AbstractField;
import cn.cerc.ui.fields.ImageConfigImpl;
import cn.cerc.ui.ssr.core.ISupplierBlock;
import cn.cerc.ui.ssr.core.SsrBlock;
import cn.cerc.ui.ssr.editor.ISsrBoard;

@Deprecated
public class SupplierField implements ISupplierBlock {
    protected boolean readonly = false;
    protected boolean required = false;
    protected String placeholder = "false";
    protected String patten = "false";
    protected String dialog = "false";
    protected boolean autofocus = false;
    protected String mark = "";
    private String type = "text";
    protected String title;
    protected String field;
    protected Map<String, String> options = new LinkedHashMap<String, String>();
    private SsrBlock block;
    private static final ClassConfig FieldConfig = new ClassConfig(AbstractField.class, SummerUI.ID);

    public SupplierField(String title, String field) {
        this.title = title;
        this.field = field;
        block = new SsrBlock();
    }

    @Override
    public SsrBlock request(ISsrBoard form) {
        var ssr = form.addBlock(title,
                String.format(
                        """
                                <li>
                                    <label for="%s"${if _mark} class='formMark'${endif}>${if required}<font role="require">*</font>${endif}<em>%s</em></label>
                                    <div>
                                        <input type="%s" name="%s" id="%s" value="${%s}" autocomplete="off"${if readonly} readonly${endif}${if autofocus} autofocus${endif}
                                        ${if placeholder} placeholder="${placeholder}"${else} placeholder="请${if dialog}点击获取${else}输入${endif}%s"${endif}${if pattern} pattern="${pattern}"${endif}${if required} required${endif} />
                                        <span role="suffix-icon">${if dialog}<a href="javascript:%s">
                                                <img src="${dialogIcon}" />
                                            </a>${endif}</span>
                                    </div>
                                </li>
                                ${if _mark}
                                <li role="%s" class="liWord" style="display: none;">
                                    <mark>%s</mark>
                                </li>
                                ${endif}
                                """,
                        field, title, type, field, field, field, title, dialog, field, mark));
        initProperty(ssr);
        return ssr;
    }

    public SupplierField readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public SupplierField required(boolean required) {
        this.required = required;
        return this;
    }

    public SupplierField placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public SupplierField patten(String patten) {
        this.patten = patten;
        return this;
    }

    public SupplierField dialog(String... dialogFunc) {
        this.dialog = getDialogText(field, dialogFunc);
        String fieldDialogIcon;
        var impl = Application.getBean(ImageConfigImpl.class);
        if (impl != null) {
            fieldDialogIcon = impl.getClassProperty(AbstractField.class, SummerUI.ID, "icon", "");
        } else {
            fieldDialogIcon = FieldConfig.getClassProperty("icon", "");
        }
        option("dialogIcon", fieldDialogIcon);
        return this;
    }

    public SupplierField autofocus(boolean autofocus) {
        this.autofocus = autofocus;
        return this;
    }

    public SupplierField type(String type) {
        this.type = type;
        return this;
    }

    public SupplierField mark(String mark) {
        this.mark = mark;
        return this;
    }

    public static final String getDialogText(String field, String... dialogFunc) {
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

    @Deprecated
    public SupplierField options(String key, String value) {
        return option(key, value);
    }

    public SupplierField option(String key, String value) {
        this.options.put(key, value);
        return this;
    }

    protected void initProperty(SsrBlock block) {
        for (String key : options.keySet())
            block.option(key, options.get(key));
        block.option("readonly", "" + readonly);
        block.option("_mark", mark);
        block.option("required", "" + required);
        block.option("placeholder", placeholder);
        block.option("pattern", patten);
        block.option("dialog", dialog);
        block.option("autofocus", "" + autofocus);
        block.fields(field).display(1);
        block.id(title);
    }

    public SsrBlock block() {
        return block;
    }

}