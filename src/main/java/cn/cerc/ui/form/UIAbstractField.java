package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.DataSource;
import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.SearchSource;
import cn.cerc.ui.core.UIComponent;

public class UIAbstractField extends UIComponent {
    public int MAX_GRID_NUM = 12;
    // 栅格布局宽度，默认为4（三分之一）
    private int width = 4;
    // 表单元素标题
    private String name;
    // 表单元素name属性
    private String code;
    // 是否必填
    private boolean require = false;
    // 开窗
    private UIDialogField dialog;

    // 数据源
    private DataSource source;

    public UIAbstractField(UIComponent owner) {
        super(owner);
        // 查找最近的数据源
        UIComponent root = owner;
        while (root != null) {
            if (root instanceof DataSource) {
                this.source = (DataSource) root;
                break;
            }
            root = root.getOwner();
        }
    }

    public UIAbstractField(UIComponent owner, String code) {
        this(owner);
        this.setCode(code);
    }

    public UIAbstractField(UIComponent owner, String code, String name) {
        this(owner);
        this.setCode(code);
        this.setName(name);
    }

    public UIAbstractField(UIComponent owner, String code, String name, int width) {
        this(owner);
        this.setCode(code);
        this.setName(name);
        this.setWidth(width);
    }

    @Override
    public void output(HtmlWriter html) {
        if (this.source == null) {
            throw new RuntimeException("source is null.");
        }
        html.print("<div class='formEdit'>");
        if (!Utils.isEmpty(this.getName())) {
            if (this.require)
                html.print("<span class='requireMark'>*</span>");
            html.print("<label for='%s'>%s</label>", this.getCode(), this.getName());
        }
        this.writeContent(html);
        html.println("</div>");
        if (this.dialog != null) {
            html.println("<span class='dialogSpan' onclick='%s'>%s</span>", this.dialog.toString(),
                    this.dialog.getText());
        }
    }

    public void writeContent(HtmlWriter html) {
        html.print("UIAbstractColumn not support.");
    }

    public int getWidth() {
        return width;
    }

    public UIAbstractField setWidth(int width) {
        this.width = width;
        return this;
    }

    public String getName() {
        return name;
    }

    public UIAbstractField setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public UIAbstractField setCode(String code) {
        this.code = code;
        return this;
    }

    public DataRow current() {
        return source != null ? source.current() : new DataRow();
    }

    public boolean isRequire() {
        return require;
    }

    public UIAbstractField setRequire(boolean require) {
        this.require = require;
        return this;
    }

    public UIAbstractField setDialog(String dialogFuction) {
        if (this.dialog == null)
            this.dialog = new UIDialogField(this.code, dialogFuction);
        this.dialog.setDialogFunc(dialogFuction);
        this.dialog.setInputId(this.code);
        return this;
    }

    public UIAbstractField setDialog(String dialogFunction, String... params) {
        setDialog(dialogFunction);
        for (String param : params) {
            this.dialog.add(param);
        }
        return this;
    }

    public UIAbstractField setDialogText(String text) {
        if (this.dialog == null)
            this.dialog = new UIDialogField(this.code);
        this.dialog.setText(text);
        return this;
    }

    public void updateField() {
        this.updateValue(this.getId(), this.code);
    }

    public void updateValue(String id, String code) {
        if (source instanceof SearchSource)
            ((SearchSource) source).updateValue(id, code);
    }
}
