package cn.cerc.ui.ssr;

public class SupplierDateRangeField extends SupplierField {
    private String beginDate;
    private String endDate;

    public SupplierDateRangeField(String title, String beginDate, String endDate) {
        super(title, beginDate);
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    @Override
    public SsrBlockImpl request(SsrComponentImpl form) {
        var ssr = form.addBlock(title, String.format("""
                <li>
                    <label for="start_date_"><em>%s</em></label>
                    <div class="dateArea">
                        <input autocomplete="off" name="%s" id="%s" type="text" class="dateAreaInput" value="${%s}"
                        ${if pattern}pattern="${pattern}"${endif} ${if required}required${endif}
                        ${if placeholder}placeholder="${placeholder}"${endif} />
                        <span>/</span>
                        <input autocomplete="off" name="%s" id="%s" type="text" class="dateAreaInput" value="${%s}"
                        ${if pattern}pattern="${pattern}"${endif} ${if required}required${endif}
                        ${if placeholder}placeholder="${placeholder}"${endif} />
                        <span role="suffix-icon">
                            <a href="javascript:showDateAreaDialog('%s', '%s')">
                            <img src="${dialogIcon}" />
                            </a>
                        </span>
                    </div>
                </li>
                """, title, beginDate, beginDate, beginDate, endDate, endDate, endDate, beginDate, endDate));
        initProperty(ssr);
        ssr.fields(String.format("%s,%s", beginDate, endDate));
        return ssr;
    }

}
