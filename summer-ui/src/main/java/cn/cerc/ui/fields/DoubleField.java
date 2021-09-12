package cn.cerc.ui.fields;

import java.text.DecimalFormat;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UrlRecord;
import cn.cerc.ui.fields.editor.ColumnEditor;

public class DoubleField extends AbstractField implements IFormatColumn {
    private ColumnEditor editor;
    private String format = "0.####";

    public DoubleField(UIComponent owner, String title, String field) {
        super(owner, title, field, 4);
        this.setAlign("right");
    }

    public DoubleField(UIComponent owner, String title, String field, int width) {
        super(owner, title, field, width);
        this.setAlign("right");
    }

    @Override
    public String getText() {
        if (buildText != null) {
            HtmlWriter html = new HtmlWriter();
            buildText.outputText(getCurrent(), html);
            return html.toString();
        }
        try {
            double val = getCurrent().getDouble(this.getField());
            DecimalFormat df = new DecimalFormat(format);
            return df.format(val);
        } catch (NumberFormatException e) {
            return "0";
        }
    }

    @Override
    public Title createTitle() {
        Title title = super.createTitle();
        title.setType("float");
        return title;
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

    public String getFormat() {
        return format;
    }

    public DoubleField setFormat(String format) {
        this.format = format;
        return this;
    }
}
