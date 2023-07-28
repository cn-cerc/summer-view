package cn.cerc.ui.style;

public class SupplierCodeNameField extends SupplierField {
    private String dialogFunc;

    public SupplierCodeNameField(String title, String field, String... dialogFunc) {
        super(title, field);
        this.dialogFunc = getDialogText(String.format("%s,%s_name", field, field), dialogFunc);
        this.readonly = true;
    }

    @Override
    public SsrTemplateImpl request(SsrComponentImpl form) {
        var ssr = form.addTemplate(title,
                String.format(
                        """
                                <li>
                                    <label for="%s_name"><em>%s</em></label>
                                    <div>
                                        <input type="hidden" name="%s" id="%s" value="${%s}">
                                        <input type="text" name="%s_name" id="%s_name" value="${%s_name}" autocomplete="off" placeholder="请点击获取%s"${if readonly} readonly${endif}>
                                        <span role="suffix-icon">
                                            <a href="javascript:%s">
                                                <img src="${dialogIcon}">
                                            </a>
                                        </span>
                                    </div>
                                </li>
                                """,
                        field, title, field, field, field, field, field, field, title, dialogFunc));
        initProperty(ssr);
        ssr.fields(String.format("%s,%s_name", field, field));
        return ssr;
    }

}
