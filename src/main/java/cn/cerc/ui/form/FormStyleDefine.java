package cn.cerc.ui.form;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;

public class FormStyleDefine implements FormStyleImpl {
    protected StringBuilder builder = new StringBuilder();
    // 表单元素标题
    protected String name;
    // 表单元素name属性
    protected String code;
    // 数据源
    protected DataRow dataRow;
    // 是否只读
    protected boolean require = false;
    // 开窗
    protected UIDialogField dialog;
    protected int width = 6;// 数据源

    public FormStyleDefine(String code, DataRow dataRow) {
        this.code = code;
        this.dataRow = dataRow;
    }

    public FormStyleDefine(String name, String code, DataRow dataRow) {
        this.name = name;
        this.code = code;
        this.dataRow = dataRow;
    }

    public FormStyleDefine(String name, String code, int width, DataRow dataRow) {
        this.name = name;
        this.code = code;
        this.width = width;
        this.dataRow = dataRow;
    }

    @Override
    public String getHtml(int width) {
        builder.append(String.format("<div class='formEdit' role='col%s'>", this.width > width ? width : this.width));
        // 构建label
        if (!Utils.isEmpty(name)) {
            builder.append(String.format("<label for='%s'>", code));
            if (require)
                builder.append("<span class='requireMark'>*</span>");
            builder.append(String.format("%s：</label>", name));
        }
        buildHtml();
        if (dialog != null) {
            builder.append(String.format("<span class='dialogSpan' onclick='%s'>%s</span>", this.dialog.toString(),
                    this.dialog.getText()));
        }
        builder.append("</div>");
        return builder.toString();
    }

    public void buildHtml() {
        builder.append("FormStyleDefine not support.");
    }

    @Override
    public int getWidth() {
        return width;
    }

    public FormStyleDefine setWidth(int width) {
        this.width = width;
        return this;
    }

    public String getName() {
        return name;
    }

    public FormStyleDefine setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public List<String> getCodes() {
        List<String> list = new ArrayList<>();
        list.add(code);
        return list;
    }

    public FormStyleDefine setCode(String code) {
        this.code = code;
        return this;
    }

    public boolean isRequire() {
        return require;
    }

    public FormStyleDefine setRequire(boolean require) {
        this.require = require;
        return this;
    }

    public FormStyleDefine setDialog(String dialogFuction) {
        if (dialog == null)
            dialog = new UIDialogField(code, dialogFuction);
        dialog.setDialogFunc(dialogFuction);
        dialog.setInputId(code);
        return this;
    }

    public FormStyleDefine setDialog(String dialogFunction, String... params) {
        setDialog(dialogFunction);
        for (String param : params) {
            dialog.add(param);
        }
        return this;
    }

    public FormStyleDefine setDialogText(String text, String dialogFuction) {
        this.setDialog(dialogFuction);
        dialog.setText(text);
        return this;
    }

    public FormStyleDefine setDialogText(String text) {
        if (dialog == null)
            dialog = new UIDialogField(this.code);
        dialog.setText(text);
        return this;
    }

}
