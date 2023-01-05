package cn.cerc.ui.form;

import cn.cerc.db.core.DataRow;
import cn.cerc.db.core.Utils;

public class FormTextAreaStyle extends FormStyleDefine {
    private int MaxSize = 0;
    // 默认行数
    private int rows = 2;
    // 输入提示文字
    private String placeholder;

    public FormTextAreaStyle(String code, DataRow data) {
        super(code, data);
    }

    public FormTextAreaStyle(String name, String code, DataRow data) {
        super(name, code, data);
    }

    public FormTextAreaStyle(String name, String code, int width, DataRow data) {
        super(name, code, width, data);
    }

    @Override
    public void buildHtml() {
        boolean showMax = MaxSize > 0;
        if (showMax) {
            builder.append("<div class='textareaBox'>");
        }
        builder.append(String.format("<textarea name='%s' rows='%s'", code, rows));
        if (showMax)
            builder.append(" oninput='updateTextArea(this)'");
        if (!Utils.isEmpty(placeholder))
            builder.append(String.format(" placeholder='%s'", placeholder));
        builder.append(String.format(">%s</textarea>", dataRow.getString(code)));
        if (showMax) {
            builder.append(
                    String.format("<span><i>%s</i>/<i>%s</i></span>", dataRow.getString(code).length(), MaxSize));
            builder.append("</div>");
        }
    }

    public int getMaxSize() {
        return MaxSize;
    }

    public FormTextAreaStyle setMaxSize(int maxSize) {
        MaxSize = maxSize;
        return this;
    }

    public int getRows() {
        return rows;
    }

    public FormTextAreaStyle setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public FormTextAreaStyle setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }
}
