package cn.cerc.ui.parts;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIImage;
import cn.cerc.ui.vcl.UISpan;
import cn.cerc.ui.vcl.ext.UIBottom;

/**
 * ·提示信息列表
 */
public class UIInfoList extends UIComponent {
    private List<Line> items = new ArrayList<>();
    private String onClick;

    public UIInfoList(UIComponent owner) {
        super(owner);
        this.setCssClass("infoList");
    }

    public Line setTitle(String title) {
        Line line = new Line();
        line.setTitle(title);
        items.add(line);
        return line;
    }

    public Line setTitle(String imgSrc, String title) {
        Line line = new Line();
        line.setTitle(imgSrc, title);
        items.add(line);
        return line;
    }

    public Line setTitle(String title, String operaText, String href) {
        Line line = new Line();
        line.setTitle(title);
        line.setRightOpera(operaText, href);
        items.add(line);
        return line;
    }

    public Line setTitle(String imgSrc, String title, String operaText, String href) {
        Line line = new Line();
        line.setTitle(imgSrc, title);
        line.setRightOpera(operaText, href);
        items.add(line);
        return line;
    }

    public Line addLine(String context) {
        return addLine(context, "");
    }

    public Line newLine() {
        Line line = new Line();
        items.add(line);
        return line;
    }

    public Line addLine(String context, String role) {
        Line line = new Line();
        line.setLineContent(context).setRole(role);
        items.add(line);
        return line;
    }

    public UIInfoList setOnClick(String onClick) {
        this.onClick = onClick;
        return this;
    }

    public UIInfoList setClickUrl(String url) {
        this.onClick = String.format("window.location.href=\"%s\";", url);
        return this;
    }

    // 在一行里设置多个组件
    public UIInfoList addLineUIComponent(UIComponent... components) {
        if (components == null || components.length == 0) {
            throw new RuntimeException("components array null or length is 0");
        }
        Line line = this.newLine();
        for (UIComponent component : components) {
            line.addComponent(component);
        }
        return this;
    }

    @Override
    public void output(HtmlWriter html) {
        html.println("<!-- %s -->", this.getClass().getName());
        html.print("<div");
        super.outputPropertys(html);
        if (this.onClick != null) {
            html.print(" onclick='%s'", this.onClick);
        }
        html.print(">");
        html.println("<ul>");
        for (UIComponent item : items) {
            item.output(html);
        }
        html.println("</ul>");
        html.println("</div>");
    }

    public class Line extends UIComponent {

        @Deprecated
        public Line() {
            this(null);
        }

        public Line(UIComponent owner) {
            super(owner);
            this.setRootLabel("li");
        }

        public UISpan setLineContent(String text) {
            UISpan uiText = new UISpan(this);
            uiText.setText(text);
            return uiText;
        }

        public Line setTitle(String title) {
            UISpan uiTitle = new UISpan(this);
            uiTitle.setText(title);
            uiTitle.setRole("title");
            return this;
        }

        public Line setTitle(String imgSrc, String title) {
            UIImage img = new UIImage(this);
            img.setSrc(imgSrc);
            setTitle(title);
            return this;
        }

        public Line addOpera(String text, String href) {
            UIBottom bottom = new UIBottom(this);
            bottom.setCaption(text).setUrl(href).setTarget(href).setCssClass("commonlyMenu");
            return this;
        }

        public Line setRightOpera(String text, String href) {
            UIBottom bottom = new UIBottom(this);
            bottom.setCaption(text);
            bottom.setUrl(href);
            return this;
        }

    }
}
