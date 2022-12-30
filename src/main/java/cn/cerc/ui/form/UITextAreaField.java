package cn.cerc.ui.form;

import cn.cerc.db.core.Utils;
import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

public class UITextAreaField extends UIAbstractField {
    private Integer MaxSize;
    // 输入提示文字
    private String placeholder;
    // 默认行数
    private int rows = 2;

    public UITextAreaField(UIComponent owner, String code) {
        super(owner, code);
    }

    public UITextAreaField(UIComponent owner, String code, String name) {
        super(owner, code, name);
    }

    public UITextAreaField(UIComponent owner, String code, String name, int width) {
        super(owner, code, name, width);
    }

    @Override
    public void writeContent(HtmlWriter html) {
        boolean showMax = MaxSize != null && MaxSize > 0;
        String code = this.getCode();
        String value = this.current().getString(code);
        if (showMax) {
            html.print("<div class='textareaBox'>");
//            if (value.length() > this.MaxSize)
//                value = value.substring(0, this.MaxSize);
        }
        html.print("<textarea name='%s' value='%s' rows='%s'", code, value, getRows());
        if (showMax)
            html.print(" oninput='updateTextArea(this)'");
        if (!Utils.isEmpty(this.placeholder))
            html.print(" placeholder='%s'", this.placeholder);
        html.print("></textarea>");
        if (showMax) {
            html.print("<span><i>%s</i>/<i>%s</i></span>", value.length(), this.MaxSize);
            html.print("</div>");
        }
    }

    public int getMaxSize() {
        return MaxSize;
    }

    public UITextAreaField setMaxSize(int size) {
        this.MaxSize = size;
        return this;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public UITextAreaField setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public int getRows() {
        return rows;
    }

    public UITextAreaField setRows(int rows) {
        this.rows = rows;
        return this;
    }

}
