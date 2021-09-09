package cn.cerc.ui.vcl;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.INameOwner;
import cn.cerc.ui.core.UIComponent;

/**
 * 多行文本输入框
 *
 * @author 黄荣君
 */
//FIXME 应改为 UITextarea，ZhangGong 2021/3/19
public class UITextarea extends UIBaseHtml implements INameOwner {
    private UISpan caption;
    private String name;
    private StringBuffer lines = new StringBuffer();
    private String placeholder;
    private int cols;// 列
    private int rows;// 行
    private String onInput;
    private boolean autofocus;
    private boolean readonly;

    public UITextarea(UIComponent owner) {
        super(owner);
    }

    public UITextarea() {
        this(null);
    }

    @Override
    public void output(HtmlWriter html) {
        if (caption != null) {
            caption.output(html);
        }

        html.print("<textarea ");
        super.outputPropertys(html);
        if (getName() != null) {
            html.print("name='%s' ", name);
        } else if (this.getId() != null) {
            html.print("name='%s' ", this.getId());
        }
        if (getRows() != 0) {
            html.print("rows='%s' ", rows);
        }
        if (getCols() != 0) {
            html.print("cols='%s' ", cols);
        }
        if (getPlaceholder() != null) {
            html.print("placeholder='%s' ", placeholder);
        }
        if (isReadonly()) {
            html.print("readonly='readonly' ");
        }
        if (getOnInput() != null) {
            html.print("oninput='%s' ", onInput);
        }
        if (isAutofocus()) {
            html.print("autofocus ");
        }
        html.print(">");

        if (getText() != null) {
            html.print(lines.toString());
        }
        html.print("</textarea>");
    }

    public UISpan getCaption() {
        if (caption == null) {
            caption = new UISpan();
        }
        return caption;
    }

    public UITextarea setCaption(UISpan caption) {
        this.caption = caption;
        return this;
    }

    @Override
    public String getName() {
        if(name == null)
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
        this.lines = new StringBuffer(text);
        return this;
    }
    
    public UITextarea append(String text) {
        this.lines = lines.append(text).append("\n");
        return this;
    }
    
    public UITextarea append(String format, Object... args) {
        return this.append(String.format(format, args));
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public UITextarea setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
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

    public boolean isReadonly() {
        return readonly;
    }

    public UITextarea setReadonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public String getOnInput() {
        return onInput;
    }

    public void setOnInput(String onInput) {
        this.onInput = onInput;
    }

    public boolean isAutofocus() {
        return autofocus;
    }

    public void setAutofocus(boolean autofocus) {
        this.autofocus = autofocus;
    }

}
