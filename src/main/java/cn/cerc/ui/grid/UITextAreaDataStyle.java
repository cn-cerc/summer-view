package cn.cerc.ui.grid;

import cn.cerc.db.core.DataCell;
import cn.cerc.db.editor.OnGetText;
import cn.cerc.ui.vcl.UITextarea;

public class UITextAreaDataStyle implements OnGetText {
    protected UIDataStyle style;
    private UITextarea textarea;

    public UITextAreaDataStyle(UIDataStyle style) {
        super();
        this.style = style;
        textarea = new UITextarea(null);
    }

    @Override
    public String getText(DataCell data) {
        FieldStyleDefine define = style.items().get(data.key());
        String defaultText = data.getString();
        if (define != null) {
            textarea.setName(data.key());
            textarea.setText(defaultText);
            textarea.setPlaceholder(define.placeholder());
            return textarea.toString();
        } else
            return defaultText;
    }

    public UITextAreaDataStyle setCols(int cols) {
        textarea.setCols(cols);
        return this;
    }

    public UITextAreaDataStyle setRows(int rows) {
        textarea.setRows(rows);
        return this;
    }

    public UITextarea getTextArea() {
        return textarea;
    }

}
