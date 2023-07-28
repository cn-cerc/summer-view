package cn.cerc.ui.style;

public class SupplierBooleanField extends SupplierField {

    public SupplierBooleanField(String title, String field) {
        super(title, field);
    }

    @Override
    public SsrTemplateImpl request(SsrComponentImpl form) {
        var ssr = form.addTemplate(title, String.format("""
                <li>
                    <div role="switch">
                        <input autocomplete="off" name="%s" id="%s" type="checkbox" value="1" ${if %s}checked ${endif}/>
                    </div>
                    <label for="%s"><em>%s</em></label>
                </li>
                        """, field, field, field, field, title));
        initProperty(ssr);
        return ssr;
    }

}
