package cn.cerc.ui.vcl;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

/*
 * 专用于简单或原始文字输出
 */
public class UIText extends UIComponent {
    private String text;
    private List<String> lines;

    public UIText() {
        this(null);
    }

    public UIText(UIComponent owner) {
        super(owner);
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        super.beginOutput(html);
        if (text != null)
            html.print(text);
        if (lines != null) {
            for (String line : lines)
                html.println("<p>%s</p>", line);
        }
    }

    public String getText() {
        return text;
    }

    public UIText setText(String text) {
        this.text = text;
        return this;
    }

    @Deprecated
    public String getContent() {
        return getText();
    }

    @Deprecated
    public UIText setContent(String text) {
        return setText(text);
    }

    @Deprecated
    public UIText setContent(String text, Object... args) {
        return setText(String.format(text, args));
    }

    public List<String> getLines() {
        if (lines == null)
            lines = new ArrayList<>();
        return lines;
    }

    public UIText add(String line) {
        this.getLines().add(line);
        return this;
    }

}
