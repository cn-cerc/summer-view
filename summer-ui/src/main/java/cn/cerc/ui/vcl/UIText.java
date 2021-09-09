package cn.cerc.ui.vcl;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.ui.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;

/*
 * 专用于简单或原始文字输出
 */
public class UIText extends UIComponent {
    private String content;
    private List<String> lines;

    public UIText() {
        super(null);
    }

    public UIText(UIComponent owner) {
        super(owner);
    }

    @Override
    public void beginOutput(HtmlWriter html) {
        super.beginOutput(html);
        if (content != null) {
            html.print(content);
        }
        if (lines != null) {
            for (String line : lines) {
                html.println("<p>%s</p>", line);
            }
        }
    }

    public String getContent() {
        return content;
    }

    public UIText setContent(String content) {
        this.content = content;
        return this;
    }

    @Deprecated
    public UIText setContent(String text, Object... args) {
        this.content = String.format(text, args);
        return this;
    }

    @Deprecated
    public List<String> getLines() {
        if (lines == null) {
            lines = new ArrayList<>();
        }
        return lines;
    }

    @Deprecated
    public UIText add(String line) {
        if (lines == null) {
            lines = new ArrayList<>();
        }
        this.lines.add(line);
        return this;
    }

}
