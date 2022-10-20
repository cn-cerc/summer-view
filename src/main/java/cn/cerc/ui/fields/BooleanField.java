package cn.cerc.ui.fields;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.editor.CheckEditor;
import cn.cerc.ui.grid.lines.AbstractGridLine.IOutputOfGridLine;
import cn.cerc.ui.other.SearchItem;
import cn.cerc.ui.vcl.UIInput;

public class BooleanField extends AbstractField implements SearchItem, IFormatColumn, IOutputOfGridLine {
    private String trueText = "是";
    private String falseText = "否";
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
        if (getBuildText() != null) {
            HtmlWriter html = new HtmlWriter();
            getBuildText().outputText(current(), html);
            return html.toString();
        } else {
            return current().getBoolean(this.getField()) ? trueText : falseText;
        }
    }

    public BooleanField setBooleanText(String trueText, String falseText) {
        this.trueText = trueText;
        this.falseText = falseText;
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        this.beginOutput(html);
        UIInput input = new UIInput(null);
        input.setId(this.getId());
        input.setName(this.getId());
        input.setValue("1");
        input.setInputType(UIInput.TYPE_CHECKBOX);
        input.setSignProperty("checked", current().getBoolean(this.getField()));
        input.setSignProperty("disabled", this.readonly());
        input.setCssProperty("onclick", this.getOnclick());
        html.print("<div class='inputContent switchContent'>");
        input.output(html);
        html.println("<span class='switch' onclick='toggleSwitch(this)'><i>%s</i><i>%s</i></span>", this.trueText, this.falseText);
        this.endOutput(html);
    }

    @Override
    public void endOutput(HtmlWriter html) {
        this.getTitle().setText(this.getName());
        this.getTitle().output(html);
        super.endOutput(html);
    }

    @Deprecated
    public BooleanField setTitle(String title) {
        this.setName(title);
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
        if (this.readonly()) {
            html.print(getText());
        } else {
            html.print(getEditor().format(current()));
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
