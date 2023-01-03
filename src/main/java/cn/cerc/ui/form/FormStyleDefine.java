package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;

public class FormStyleDefine implements FormStyleImpl {
    protected StringBuilder builder = new StringBuilder();
    protected String code;
    protected DataRow dataRow;

    public FormStyleDefine(String code, DataRow dataRow) {
        this.code = code;
        this.dataRow = dataRow;
    }

    @Override
    public String getHtml() {
        buildHtml();
        return builder.toString();
    }

    public void buildHtml() {
        builder.append("FormStyleDefine not support.");
    }

}
