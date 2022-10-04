package cn.cerc.ui.vcl;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.INameOwner;
import cn.cerc.ui.core.UIComponent;

/**
 * 多行文本输入框
 *
 * @author 黄荣君
 */
//FIXME 应改为 UITextarea，ZhangGong 2021/3/19
public class UITextarea extends UIComponent implements IHtml, INameOwner {
    private UISpan title;
    private String name;
    private UIText lines;
    private int cols;
    private int rows;

    public UITextarea() {
        this(null);
    }

    public UITextarea(UIComponent owner) {
        super(owner);
        this.setRootLabel("textarea");
        this.lines = new UIText(this);
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        if (title != null)
            title.output(html);
        this.setCssProperty("name", this.getName());
        if (getRows() != 0)
            this.setCssProperty("rows", rows);
        if (getCols() != 0)
            this.setCssProperty("cols", cols);
        super.beginOutput(html);
    }

    @Deprecated
    public UISpan getCaption() {
        return getTitle();
    }

    public UISpan getTitle() {
        if (title == null) {
            title = new UISpan(this);
        }
        return title;
    }

    @Override
    public String getName() {
        if (name == null)
            name = getId();
        return name;
    }

    public UITextarea setName(String name) {
        this.name = name;
        return this;
    }

    public String getText() {
        return lines.toString();
    }

    public UITextarea setText(String text) {
        this.lines.setText(text);
        return this;
    }

    public UITextarea append(String text) {
        this.lines.setText(lines.getText() + text + "\n");
        return this;
    }

    public UITextarea append(String format, Object... args) {
        return this.append(String.format(format, args));
    }

    public String getPlaceholder() {
        return (String) this.getCssProperty("placeholder");
    }

    public UITextarea setPlaceholder(String placeholder) {
        this.setCssProperty("placeholder", placeholder);
        return this;
    }

    public int getCols() {
        return cols;
    }

    public UITextarea setCols(int cols) {
        this.cols = cols;
        return this;
    }

    public int getRows() {
        return rows;
    }

    public UITextarea setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public UITextarea setReadonly(boolean readonly) {
        this.setSignProperty("readonly", readonly);
        return this;
    }

    public UITextarea setAutofocus(boolean autofocus) {
        this.setSignProperty("autofocus", autofocus);
        return this;
    }

    public String getOnInput() {
        return (String) this.getCssProperty("oninput");
    }

    public void setOnInput(String onInput) {
        this.setCssProperty("oninput", onInput);
    }

}
