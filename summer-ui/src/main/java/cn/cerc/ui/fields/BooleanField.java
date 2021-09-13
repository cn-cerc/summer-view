package cn.cerc.ui.fields;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.editor.CheckEditor;
import cn.cerc.ui.other.SearchItem;

public class BooleanField extends AbstractField implements SearchItem, IFormatColumn {
    private String trueText = "是";
    private String falseText = "否";
    private String title;
    private boolean search;
    private CheckEditor editor;

    public BooleanField(UIComponent owner, String title, String field) {
        this(owner, title, field, 0);
    }

    public BooleanField(UIComponent owner, String title, String field, int width) {
        super(owner, title, field, width);
        this.setAlign("center");
    }

    @Override
    public String getText() {
        if (buildText != null) {
            HtmlWriter html = new HtmlWriter();
            buildText.outputText(getCurrent(), html);
            return html.toString();
        }
        return getCurrent().getBoolean(this.getField()) ? trueText : falseText;
    }

    public BooleanField setBooleanText(String trueText, String falseText) {
        this.trueText = trueText;
        this.falseText = falseText;
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        if (!this.search) {
            html.println(String.format("<label for=\"%s\">%s</label>", this.getId(), this.getName() + "："));
            writeInput(html);
            if (this.title != null) {
                html.print("<label for=\"%s\">%s</label>", this.getId(), this.title);
            }
        } else {
            writeInput(html);
            html.println("<label for=\"%s\">%s</label>", this.getId(), this.getName());
        }
    }

    private void writeInput(HtmlWriter html) {
        html.print(String.format("<input type=\"checkbox\" id=\"%s\" name=\"%s\" value=\"1\"", this.getId(),
                this.getId()));
        if (getCurrent().getBoolean(this.getField())) {
            html.print(" checked");
        }
        if (this.isReadonly()) {
            html.print(" disabled");
        }
        if (this.onclick != null) {
            html.print(" onclick=\"%s\"", this.onclick);
        }
        html.print(">");
    }

    @Override
    public String getTitle() {
        return title == null ? this.getName() : title;
    }

    public BooleanField setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isSearch() {
        return search;
    }

    @Override
    public void setSearch(boolean search) {
        this.search = search;
    }

    @Override
    public void outputOfGridLine(HtmlWriter html) {
        if (this.isReadonly()) {
            html.print(getText());
        } else {
            html.print(getEditor().format(getCurrent()));
        }
    }

    public CheckEditor getEditor() {
        if (editor == null) {
            editor = new CheckEditor(this);
        }
        return editor;
    }

    public String getTrueText() {
        return trueText;
    }

    public void setTrueText(String trueText) {
        this.trueText = trueText;
    }

    public String getFalseText() {
        return falseText;
    }

    public void setFalseText(String falseText) {
        this.falseText = falseText;
    }

}
