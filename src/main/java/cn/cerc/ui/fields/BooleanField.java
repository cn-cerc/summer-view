package cn.cerc.ui.fields;

import java.util.function.Supplier;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.fields.editor.CheckEditor;
import cn.cerc.ui.grid.IOutputOfGridHead;
import cn.cerc.ui.grid.lines.AbstractGridLine.IOutputOfGridLine;
import cn.cerc.ui.other.SearchItem;
import cn.cerc.ui.vcl.UIInput;
import cn.cerc.ui.vcl.UILabel;
import cn.cerc.ui.vcl.UIText;

public class BooleanField extends AbstractField
        implements SearchItem, IFormatColumn, IOutputOfGridLine, IOutputOfGridHead {
    private String trueText = "是";
    private String falseText = "否";
    private boolean search;
    private CheckEditor editor;
    // 是否允许全选
    private boolean allowCheckedAll = false;
    // 非只读模式下自定义value
    private Supplier<String> customValue;

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
        UIText switchBox = new UIText(null);
        switchBox.setRootLabel("div");
        switchBox.setCssProperty("role", "switch");
        UIInput input = new UIInput(switchBox);
        input.setId(this.getId());
        input.setName(this.getId());
        input.setValue("1");
        input.setInputType(UIInput.TYPE_CHECKBOX);
        if (current() != null)
            input.setSignProperty("checked", current().getBoolean(this.getField()));
        input.setSignProperty("disabled", this.readonly());
        input.setCssProperty("onclick", this.getOnclick());
        switchBox.output(html);
        this.endOutput(html);
    }

    @Override
    public void endOutput(HtmlWriter html) {
        UILabel label = this.getTitle();
        if (this.getMark() != null)
            label.setCssClass("formMark");
        if (this.getWordId() != null) {
            label.setCssClass("formMark");
            label.setCssProperty("wordId", this.getWordId());
        }
        label.setFor(this.getId()).setText(String.format("<em>%s</em>", this.getName()));
        if (this.isShowStar())
            new UIStarFlag(label);
        label.output(html);
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
            CheckEditor editor = getEditor();
            if (this.customValue != null)
                editor.setValue(getCustomValue().get());
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

    public BooleanField setTrueText(String trueText) {
        this.trueText = trueText;
        return this;
    }

    public String getFalseText() {
        return falseText;
    }

    public BooleanField setFalseText(String falseText) {
        this.falseText = falseText;
        return this;
    }

    @Override
    public void updateField() {
        if (!this.readonly()) {
            super.updateField();
        }
    }

    public boolean allowCheckedAll() {
        return allowCheckedAll;
    }

    public BooleanField allowCheckedAll(boolean allowCheckedAll) {
        this.allowCheckedAll = allowCheckedAll;
        return this;
    }

    @Override
    public void outputOfGridHead(HtmlWriter html) {
        if (!this.readonly() && this.allowCheckedAll()) {
            html.print("<div><input type='checkbox' data-field='%s' onchange='handleGridSelectAll(this)' /></div>",
                    this.getField(), this.getField());
        } else {
            html.print("<div>%s</div>", this.getName());
        }
    }

    public Supplier<String> getCustomValue() {
        return customValue;
    }

    public BooleanField setCustomValue(Supplier<String> customValue) {
        this.customValue = customValue;
        return this;
    }

}
