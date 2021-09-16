package cn.cerc.ui.fields;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.editor.ColumnEditor;
import cn.cerc.ui.grid.lines.AbstractGridLine.IOutputOfGridLine;
import cn.cerc.ui.vcl.UIUrl;

public class StringField extends AbstractField implements IFormatColumn, IOutputOfGridLine {
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
                UIUrl url = new UIUrl(null);
                buildUrl.buildUrl(getCurrent(), url);
                if (!"".equals(url.getHref())) {
                    url.setText(getText());
                    url.output(html);
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
