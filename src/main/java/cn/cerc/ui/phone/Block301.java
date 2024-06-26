package cn.cerc.ui.phone;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.core.UrlRecord;
import cn.cerc.ui.vcl.UIImage;

/**
 * 用于生成供应商、客户、帐套选择
 */
public class Block301 extends UICustomPhone {
    List<UIImage> list = new ArrayList<>();
    private UIImage leftIcon = new UIImage();
    private String title = "(title)";
    private UIImage rightIcon = new UIImage();
    private UrlRecord operator;
    private Map<String, String> items = new LinkedHashMap<>();

    public Block301(UIComponent owner) {
        super(owner);
        this.setCssClass("block301");
        operator = new UrlRecord();
        leftIcon.setSrc(getProductImage("images/logo/logo_default.png"));
        leftIcon.setRole("icon");

        rightIcon.setSrc(getImage("jui/phone/block301-rightIcon.png"));
        rightIcon.setRole("right");
    }

    @Override
    public void output(HtmlWriter html) {
        html.println("<!-- %s -->", this.getClass().getName());
        html.print("<div");
        outputPropertys(html);
        html.print("'>");
        leftIcon.output(html);
        html.print("<a href='%s'>", operator.getUrl());
        html.print("<div>");

        html.print("<div role='title'>");
        html.print("<span role='title'>");
        for (UIImage image : list) {
            html.print(image.toString());
        }
        html.print("%s</span>", title);

        rightIcon.output(html);
        html.print("</div>");

        html.print("<div role='describe'>");
        int i = 0;
        for (String key : items.keySet()) {
            html.println("<span>%s：%s</span>", key, items.get(key));
            i++;
            if (i % 2 == 0) {
                html.println("<br />");
            }
        }
        html.print("</div>");

        html.print("</div>");
        html.print("</a>");
        html.print("<div style='clear: both'></div>");
        html.println("</div>");
    }

    public UIImage getLeftIcon() {
        return leftIcon;
    }

    public String getTitle() {
        return title;
    }

    public Block301 setTitle(String format, Object... args) {
        this.title = String.format(format, args);
        return this;
    }

    public UrlRecord getOperator() {
        return operator;
    }

    public Block301 setOperator(UrlRecord operator) {
        this.operator = operator;
        return this;
    }

    public UIImage getRightIcon() {
        return rightIcon;
    }

    public Block301 addIcon(UIImage img) {
        list.add(img);
        return this;
    }

    public Block301 add(String key, String value) {
        items.put(key, value);
        return this;
    }

}
