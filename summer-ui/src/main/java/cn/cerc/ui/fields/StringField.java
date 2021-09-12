package cn.cerc.ui.fields;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UrlRecord;
import cn.cerc.ui.fields.editor.ColumnEditor;

public class StringField extends AbstractField implements IFormatColumn {
    private ColumnEditor editor;

    public StringField(UIComponent owner, String name, String field) {
        super(owner, name, field);
    }

    public StringField(UIComponent owner, String name, String field, int width) {
        super(owner, name, field);
        this.setWidth(width);
    }

    @Override
    public void outputOfGridLine(HtmlWriter html) {
        if (this.isReadonly()) {
            if (buildUrl != null) {
                UrlRecord url = new UrlRecord();
                buildUrl.buildUrl(getCurrent(), url);
                if (!"".equals(url.getUrl())) {
                    html.print("<a href=\"%s\"", url.getUrl());
                    if (url.getTitle() != null) {
                        html.print(" title=\"%s\"", url.getTitle());
                    }
                    if (url.getTarget() != null) {
                        html.print(" target=\"%s\"", url.getTarget());
                    }
                    html.println(">%s</a>", getText());
                } else {
                    html.println(getText());
                }
            } else {
                html.print(getText());
            }
        } else {
            html.print(getEditor().format(getCurrent()));
        }
    }

    public ColumnEditor getEditor() {
        if (editor == null) {
            editor = new ColumnEditor(this);
        }
        return editor;
    }
}
