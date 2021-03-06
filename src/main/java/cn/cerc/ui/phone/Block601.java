package cn.cerc.ui.phone;

import java.util.ArrayList;
import java.util.List;

import cn.cerc.mis.core.HtmlWriter;
import cn.cerc.ui.core.UIComponent;
import cn.cerc.ui.vcl.UIImage;

/**
 * @author 张弓
 */
public class Block601 extends UICustomPhone {
    private List<UIImage> items = new ArrayList<>();

    /**
     * 多图片显示，左右滑动更换
     *
     * @param owner 内容显示区
     */
    public Block601(UIComponent owner) {
        super(owner);
    }

    @Override
    public void output(HtmlWriter html) {
        if (items.size() == 0) {
            UIImage image = new UIImage();
            image.setAlt("(image)");
            image.setWidth("100%").setHeight("192px");
            image.setSrc("");
            items.add(image);
        }
        html.println("<!-- %s -->", this.getClass().getName());
        html.println("<div class=\"block601\">");
        html.println("<div class=\"swiper-wrapper\">");
        for (UIImage image : items) {
            html.println("<div class=\"swiper-slide\">");
            image.output(html);
            html.println("</div>");
        }
        html.println("</div>");
        html.println("<div class=\"swiper-pagination\"></div>");
        html.println("</div>");
    }

    public UIImage addImage(String imgUrl) {
        UIImage image = new UIImage();
        image.setSrc(imgUrl);
        items.add(image);
        return image;
    }

    /**
     * 针对图片进行阿里云OSS压缩
     * 
     * @param imgUrl 图片地址
     * @param width  图片宽度
     * @return 压缩后的图片
     */
    public UIImage addImage(String imgUrl, int width) {
        UIImage image = new UIImage();
        image.setSrc(imgUrl, width);
        items.add(image);
        return image;
    }
}
